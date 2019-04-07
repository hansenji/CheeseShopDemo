package com.vikingsen.cheesedemo.model.repository.comment

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.vikingsen.cheesedemo.model.database.ShopDatabaseWrapper
import com.vikingsen.cheesedemo.model.database.comment.Comment
import com.vikingsen.cheesedemo.model.webservice.dto.CommentDto
import com.vikingsen.cheesedemo.model.webservice.dto.CommentResponse
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentLocalDataSource
@Inject constructor(
    private val shopDatabaseWrapper: ShopDatabaseWrapper
) {
    fun getComments(cheeseId: Long): LiveData<List<Comment>> {
        return shopDatabaseWrapper.getDatabase().commentDao.findAllByCheeseId(cheeseId)
    }

    fun saveCommentsFromServer(commentDtos: List<CommentDto>) {
        val commentDao = shopDatabaseWrapper.getDatabase().commentDao
        val cached = LocalDateTime.now()
        val comments = commentDtos.map {
            val comment = commentDao.findById(it.guid) ?: Comment(it.guid)
            with(comment) {
                this.cheeseId = it.cheeseId
                this.user = it.user
                this.comment = it.comment
                this.synced = true
                this.cached = cached
            }
            return@map comment
        }
        commentDao.insertAll(comments)
    }

    @WorkerThread
    fun saveNewComment(cheeseId: Long, user: String, comment: String) {
        shopDatabaseWrapper.getDatabase().commentDao.insert(
            Comment(UUID.randomUUID().toString(), cheeseId, user, comment, OffsetDateTime.now())
        )
    }

    @WorkerThread
    fun getNotSyncedComments() = shopDatabaseWrapper.getDatabase().commentDao.findAllNotSynced()

    @WorkerThread
    fun saveSyncResponses(responses: List<CommentResponse>): Boolean {
        val cached = LocalDateTime.now()
        try {
            val database = shopDatabaseWrapper.getDatabase()
            database.runInTransaction {
                responses.filter { it.isSuccessful }
                    .forEach { database.commentDao.setSynced(it.guid, cached) }
            }
        } catch (e: Exception) {
            Timber.w(e, "Failed to save sync responses")
        }
        return true
    }

}