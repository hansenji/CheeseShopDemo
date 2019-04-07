package com.vikingsen.cheesedemo.model.repository.comment

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.vikingsen.cheesedemo.BuildConfig
import com.vikingsen.cheesedemo.model.NetworkBoundResource
import com.vikingsen.cheesedemo.model.Resource
import com.vikingsen.cheesedemo.model.database.comment.Comment
import com.vikingsen.cheesedemo.model.webservice.dto.CommentDto
import com.vikingsen.cheesedemo.model.webservice.dto.CommentRequestDto
import com.vikingsen.cheesedemo.model.work.WorkScheduler
import com.vikingsen.cheesedemo.util.CoroutineContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository
@Inject constructor(
    private val remoteDataSource: CommentRemoteDataSource,
    private val localDataSource: CommentLocalDataSource,
    private val workScheduler: WorkScheduler,
    private val cc: CoroutineContextProvider
) {
    fun getComments(cheeseId: Long, forceRefresh: Boolean): LiveData<Resource<List<Comment>>> =
        object : NetworkBoundResource<List<Comment>, List<CommentDto>>() {
            override fun loadFromDb() = localDataSource.getComments(cheeseId)

            override suspend fun CoroutineScope.shouldFetch(data: List<Comment>?): Boolean {
                if (forceRefresh || data == null) {
                    return true
                }

                val cacheExpiration = LocalDateTime.now().minus(CACHE_VALID_AMOUNT, CACHE_VALID_UNIT)
                // GOTCHA - DOUBLE CHECK THAT THE CHECK MATCHES THE METHOD NAME (isBefore vs isAfter)
                return data.mapNotNull { it.cached }.min()?.isBefore(cacheExpiration) ?: true
            }

            override suspend fun CoroutineScope.fetchFromNetwork(): NetworkResponse<List<CommentDto>> {
                return remoteDataSource.getComments(cheeseId)
            }

            override suspend fun CoroutineScope.saveNetworkData(data: List<CommentDto>) {
                localDataSource.saveCommentsFromServer(data)
            }

        }.asLiveData()

    fun addComment(cheeseId: Long, comment: String) = GlobalScope.launch(cc.io) {
        localDataSource.saveNewComment(cheeseId, BuildConfig.USER_NAME, comment)
        workScheduler.scheduleCommentSync()
    }

    @WorkerThread
    suspend fun syncComments(): Boolean {
        val comments = localDataSource.getNotSyncedComments()
            .map { CommentRequestDto(it.id, it.cheeseId, it.user, it.comment) }
        val responses = remoteDataSource.syncComments(comments) ?: return false
        return localDataSource.saveSyncResponses(responses)
    }

    companion object {
        private const val CACHE_VALID_AMOUNT = 1L
        private val CACHE_VALID_UNIT = ChronoUnit.DAYS
    }
}