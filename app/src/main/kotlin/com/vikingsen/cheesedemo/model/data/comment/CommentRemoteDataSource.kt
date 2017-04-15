package com.vikingsen.cheesedemo.model.data.comment

import android.support.annotation.WorkerThread
import com.vikingsen.cheesedemo.model.webservice.CheeseService
import com.vikingsen.cheesedemo.model.webservice.dto.CommentDto
import com.vikingsen.cheesedemo.model.webservice.dto.CommentRequestDto
import com.vikingsen.cheesedemo.model.webservice.dto.CommentResponse
import com.vikingsen.cheesedemo.util.NetworkUtil
import io.reactivex.Maybe
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRemoteDataSource
@Inject constructor(private val cheeseService: CheeseService,
                    private val networkUtil: NetworkUtil) {

    fun getComments(cheeseId: Long): Single<List<CommentDto>> {
        return Single.create<List<CommentDto>> { emitter ->
            try {
                if (networkUtil.isConnected) {
                    val response = cheeseService.getComments(cheeseId).execute()
                    if (response.isSuccessful) {
                        emitter.onSuccess(response.body())
                        return@create
                    } else {
                        Timber.e("Failed to load comments for cheese %d (%s) : %s", cheeseId, response.code(), response.message())
                    }
                } else {
                    Timber.w("Network not connected")
                }
            } catch (e: Exception) {
                Timber.e(e, "Exception fetching comments for cheese %d", cheeseId)
            }

            emitter.onSuccess(emptyList<CommentDto>())
        }
    }

    @WorkerThread
    fun syncComments(requestDtos: List<CommentRequestDto>): Maybe<List<CommentResponse>>? {
        return Maybe.create<List<CommentResponse>> { emitter ->
            try {
                val response = cheeseService.postComment(requestDtos).execute()
                if (response.isSuccessful) {
                    emitter.onSuccess(response.body())
                    return@create
                } else {
                    Timber.e("Failed to post %d comments (%s) : %s", requestDtos.size, response.code(), response.message())
                }
            } catch (e: Exception) {
                Timber.e(e, "Exception fetching %d comments", requestDtos.size)
            }

            emitter.onComplete()
        }
    }
}
