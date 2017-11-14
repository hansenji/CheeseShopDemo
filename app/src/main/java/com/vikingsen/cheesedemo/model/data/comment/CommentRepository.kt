package com.vikingsen.cheesedemo.model.data.comment


import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.vikingsen.cheesedemo.job.AppJobScheduler
import com.vikingsen.cheesedemo.model.NetworkBoundResource
import com.vikingsen.cheesedemo.model.Resource
import com.vikingsen.cheesedemo.model.database.comment.Comment
import com.vikingsen.cheesedemo.model.webservice.dto.CommentDto
import com.vikingsen.cheesedemo.model.webservice.dto.CommentRequestDto
import com.vikingsen.cheesedemo.util.CoroutineContextProvider
import kotlinx.coroutines.experimental.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository
@Inject constructor(
        private val remoteDataSource: CommentRemoteDataSource,
        private val localDataSource: CommentLocalDataSource,
        private val appJobScheduler: AppJobScheduler,
        private val ccp: CoroutineContextProvider
) {

    fun getComments(cheeseId: Long, forceRefresh: Boolean): LiveData<Resource<List<Comment>>> =
            object : NetworkBoundResource<List<Comment>, List<CommentDto>>(ccp.commonPool) {
                override fun loadFromDb(): LiveData<List<Comment>> = localDataSource.getComments(cheeseId)

                suspend override fun shouldFetch(data: List<Comment>?): Boolean {
                    if (forceRefresh || data == null) {
                        return true
                    }

                    val cacheExpiration = LocalDateTime.now().minus(CACHE_VALID_AMOUNT, CACHE_VALID_UNIT)
                    // GOTCHA - DOUBLE CHECK THAT THE CHECK MATCHES THE METHOD NAME (isBefore vs isAfter)
                    return data.mapNotNull { it.cached }.min()?.isBefore(cacheExpiration) ?: true
                }

                suspend override fun fetchFromNetwork(): NetworkResponse<List<CommentDto>> = remoteDataSource.getComments(cheeseId)

                suspend override fun saveNetworkData(data: List<CommentDto>) {
                    localDataSource.saveCommentsFromServer(data)
                }

            }.asLiveData()

    fun addComment(cheeseId: Long, user: String, comment: String) {
        launch(ccp.commonPool) {
            localDataSource.saveNewComment(cheeseId, user, comment)
            appJobScheduler.scheduleCommentSync()
        }
    }

    @WorkerThread
    suspend fun syncComments(): Boolean {
        val comments = localDataSource.getNotSyncedComments()
                .map { CommentRequestDto(it.id, it.cheeseId, it.user, it.comment) }
        val responses = remoteDataSource.syncComments(comments) ?: return false
        return localDataSource.saveSyncResponses(responses)
    }

    companion object {
        private val CACHE_VALID_AMOUNT = 1L
        private val CACHE_VALID_UNIT = ChronoUnit.DAYS
    }
}
