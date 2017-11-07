package com.vikingsen.cheesedemo.model.data.comment

import com.vikingsen.cheesedemo.model.database.comment.Comment
import com.vikingsen.cheesedemo.model.database.comment.CommentManager
import com.vikingsen.cheesedemo.model.webservice.dto.CommentDto
import com.vikingsen.cheesedemo.model.webservice.dto.CommentResponse
import com.vikingsen.cheesedemo.util.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentLocalDataSource
@Inject constructor(private val commentManager: CommentManager,
                    private val schedulerProvider: SchedulerProvider) {

    fun getComments(cheeseId: Long): Single<List<Comment>> {
        return commentManager.findAllForCheeseIdRx(cheeseId)
    }

    fun areCommentsStale(cheeseId: Long): Boolean {
        val cacheExpiration = LocalDateTime.now().minus(CACHE_VALID_AMOUNT, CACHE_VALID_UNIT)
        return commentManager.findOldestCacheData(cheeseId).isBefore(cacheExpiration)
    }

    fun saveCommentsFromServer(commentDtos: List<CommentDto>): Boolean {
        commentManager.beginTransaction()
        val cached = LocalDateTime.now()
        var commit = false
        try {
            // TODO delete comments that are no longer valid(on the server)
            for ((guid, cheeseId, user, comment1, created) in commentDtos) {
                var comment = commentManager.findByGuid(guid)
                if (comment == null) {
                    comment = Comment()
                    comment.guid = guid
                }
                comment.cheeseId = cheeseId
                comment.user = user
                comment.comment = comment1
                comment.created = created
                comment.synced = true
                comment.cached = cached
                commentManager.save(comment)
            }
            commit = true
        } finally {
            commentManager.endTransaction(commit)
        }
        return commit
    }

    fun saveNewComment(cheeseId: Long, user: String, text: String): Completable {
        return Completable.create { emitter ->
            try {
                val comment = Comment()
                comment.guid = UUID.randomUUID().toString()
                comment.cheeseId = cheeseId
                comment.user = user
                comment.comment = text
                comment.created = LocalDateTime.now()
                if (commentManager.save(comment)) {
                    emitter.onComplete()
                } else {
                    emitter.onError(Exception("Failed to save comment " + comment.guid))
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }.subscribeOn(schedulerProvider.computation())
    }

    fun getNotSyncedComments(): Single<List<Comment>> {
        return commentManager.findAllNotSyncedRx()
    }

    fun saveSyncResponses(responses: List<CommentResponse>): Boolean {
        commentManager.beginTransaction()
        var commit = false
        val cached = LocalDateTime.now()
        try {
            responses.filter { it.isSuccessful }
                    .forEach { commentManager.setCommentSynced(it.guid, cached) }
            commit = true
        } catch (e: Exception) {
            Timber.w(e, "Failed to save sync responses")
        } finally {
            commentManager.endTransaction(commit)
        }
        return commit
    }

    /**
     * Auto subscribes on computation scheduler
     */
    fun modelChanges(): Observable<CommentChange> {
        return commentManager.tableChanges().map { tableChange ->
            return@map when {
                tableChange.isBulkOperation -> CommentChange.bulkOperation()
                else -> CommentChange.forCheese(commentManager.findCheeseId(tableChange.rowId))
            }
        }.subscribeOn(schedulerProvider.computation())
    }

    companion object {

        private val CACHE_VALID_AMOUNT = 1L
        private val CACHE_VALID_UNIT = ChronoUnit.DAYS
    }
}
