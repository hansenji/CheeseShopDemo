package com.vikingsen.cheesedemo.model.repository.comment

import androidx.annotation.WorkerThread
import com.vikingsen.cheesedemo.model.webservice.ApiResponse
import com.vikingsen.cheesedemo.model.webservice.CheeseService
import com.vikingsen.cheesedemo.model.webservice.dto.CommentDto
import com.vikingsen.cheesedemo.model.webservice.dto.CommentRequestDto
import com.vikingsen.cheesedemo.model.webservice.dto.CommentResponse
import com.vikingsen.cheesedemo.util.NetworkDisconnectedException
import com.vikingsen.cheesedemo.util.NetworkUtil
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRemoteDataSource
@Inject constructor(
    private val cheeseService: CheeseService,
    private val networkUtil: NetworkUtil
) {
    @WorkerThread
    fun getComments(cheeseId: Long): ApiResponse<List<CommentDto>> {
        try {
            if (networkUtil.isConnected()) {
                return ApiResponse(cheeseService.getComments(cheeseId).execute())
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception fetching cheese")
            return ApiResponse(e)
        }
        return ApiResponse(NetworkDisconnectedException())
    }

    @WorkerThread
    suspend fun syncComments(requestDtos: List<CommentRequestDto>): List<CommentResponse>?  =
        coroutineScope {
            try {
                val response = cheeseService.postComment(requestDtos).execute()
                if (response.isSuccessful) {
                    return@coroutineScope response.body()
                } else {
                    Timber.e("Failed to post %d comments (%s): %s", requestDtos.size, response.code(), response.message())
                }
            } catch (e: Exception) {
                Timber.e(e, "Exception posting %d comments", requestDtos.size)
            }
            return@coroutineScope null
        }

}