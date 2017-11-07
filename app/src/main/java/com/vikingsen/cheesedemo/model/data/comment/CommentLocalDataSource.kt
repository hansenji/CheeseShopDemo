package com.vikingsen.cheesedemo.model.data.comment

import com.vikingsen.cheesedemo.model.room.ShopDatabase
import com.vikingsen.cheesedemo.model.room.comment.Comment
import com.vikingsen.cheesedemo.model.room.comment.CommentDao
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
@Inject constructor(private val shopDatabase: ShopDatabase,
                    private val commentDao: CommentDao,
                    private val schedulerProvider: SchedulerProvider) {

    fun getComments(cheeseId: Long): Single<List<Comment>> {
        return commentDao.findAllByCheeseIdRx(cheeseId)
    }

    fun areCommentsStale(cheeseId: Long): Boolean {
        val cacheExpiration = LocalDateTime.now().minus(CACHE_VALID_AMOUNT, CACHE_VALID_UNIT)
        return commentDao.findOldestCacheByCheeseId(cheeseId)?.isBefore(cacheExpiration) ?: true
    }

    fun saveCommentsFromServer(commentDtos: List<CommentDto>): Boolean {
        val cached = LocalDateTime.now()
        val comments = commentDtos.map { (guid, cheeseId, user, text, created) ->
            val comment = commentDao.findById(guid) ?: Comment().apply { this.id = guid }
            with(comment) {
                this.cheeseId = cheeseId
                this.user = user
                this.comment = text
                this.created = created
                this.synced = true
                this.cached = cached
            }
            return@map comment
        }
        commentDao.insertAll(comments)
        return true
    }

    fun saveNewComment(cheeseId: Long, user: String, text: String): Completable {
        return Completable.create { emitter ->
            try {
                val comment = Comment()
                comment.id = UUID.randomUUID().toString()
                comment.cheeseId = cheeseId
                comment.user = user
                comment.comment = text
                comment.created = LocalDateTime.now()
                commentDao.insert(comment)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }.subscribeOn(schedulerProvider.computation())
    }

    fun getNotSyncedComments(): Single<List<Comment>> {
        return commentDao.findAllNotSyncedRx()
    }

    fun saveSyncResponses(responses: List<CommentResponse>): Boolean {
        val cached = LocalDateTime.now()
        try {
            shopDatabase.runInTransaction {
                responses.filter { it.isSuccessful }
                        .map { commentDao.setSynced(it.guid, cached) }
            }
        } catch (e: Exception) {
            Timber.w(e, "Failed to save sync responses")
        }
        return true
    }

    /**
     * Auto subscribes on computation scheduler
     */
    @Deprecated("Switch to LiveData", ReplaceWith(""))
    fun modelChanges(): Observable<CommentChange> {
        return Observable.empty()
    }

    companion object {
        private val CACHE_VALID_AMOUNT = 1L
        private val CACHE_VALID_UNIT = ChronoUnit.DAYS
    }
}
