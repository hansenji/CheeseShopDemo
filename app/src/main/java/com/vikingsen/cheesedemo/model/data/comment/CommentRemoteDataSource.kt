package com.vikingsen.cheesedemo.model.data.comment

import android.support.annotation.WorkerThread
import com.vikingsen.cheesedemo.model.webservice.ApiResponse
import com.vikingsen.cheesedemo.model.webservice.CheeseService
import com.vikingsen.cheesedemo.model.webservice.dto.CommentDto
import com.vikingsen.cheesedemo.model.webservice.dto.CommentRequestDto
import com.vikingsen.cheesedemo.model.webservice.dto.CommentResponse
import com.vikingsen.cheesedemo.util.NetworkDisconnectedException
import com.vikingsen.cheesedemo.util.NetworkUtil
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRemoteDataSource
@Inject constructor(private val cheeseService: CheeseService,
                    private val networkUtil: NetworkUtil) {

    fun getComments(cheeseId: Long): ApiResponse<List<CommentDto>> {
        try {
            if (networkUtil.isConnected()) {
                return ApiResponse(cheeseService.getComments(cheeseId).execute())
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception fetching cheese")
        }
        return ApiResponse(NetworkDisconnectedException())
    }

    @WorkerThread
    suspend fun syncComments(requestDtos: List<CommentRequestDto>): List<CommentResponse>? {
        try {
            val response = cheeseService.postComment(requestDtos).execute()
            if (response.isSuccessful) {
                response.body()?.let {
                    return it
                }
            } else {
                Timber.e("Failed to post %d comments (%s): %s", requestDtos.size, response.code(), response.message())
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception posting %d comments", requestDtos.size)
        }
        return null
    }
}
