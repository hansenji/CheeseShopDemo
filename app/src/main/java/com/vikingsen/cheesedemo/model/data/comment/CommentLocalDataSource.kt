package com.vikingsen.cheesedemo.model.data.comment

import android.support.annotation.WorkerThread
import com.vikingsen.cheesedemo.model.database.ShopDatabase
import com.vikingsen.cheesedemo.model.database.comment.Comment
import com.vikingsen.cheesedemo.model.database.comment.CommentDao
import com.vikingsen.cheesedemo.model.webservice.dto.CommentDto
import com.vikingsen.cheesedemo.model.webservice.dto.CommentResponse
import com.vikingsen.cheesedemo.util.CoroutineContextProvider
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentLocalDataSource
@Inject constructor(
        private val shopDatabase: ShopDatabase,
        private val commentDao: CommentDao,
        private val coroutineContextProvider: CoroutineContextProvider
) {

    fun getComments(cheeseId: Long) = commentDao.findAllByCheeseId(cheeseId)

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

    @WorkerThread
    suspend fun saveNewComment(cheeseId: Long, user: String, comment: String) {
        commentDao.insert(Comment().apply {
            this.id = UUID.randomUUID().toString()
            this.cheeseId = cheeseId
            this.user = user
            this.comment = comment
            this.created = LocalDateTime.now()
        })
    }

    @WorkerThread
    suspend fun getNotSyncedComments(): List<Comment> = commentDao.findAllNotSynced()

    @WorkerThread
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
}
