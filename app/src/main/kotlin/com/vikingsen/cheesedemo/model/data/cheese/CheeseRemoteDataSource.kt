package com.vikingsen.cheesedemo.model.data.cheese


import com.vikingsen.cheesedemo.model.webservice.CheeseService
import com.vikingsen.cheesedemo.model.webservice.dto.CheeseDto
import com.vikingsen.cheesedemo.util.NetworkUtil
import io.reactivex.Maybe
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheeseRemoteDataSource @Inject
constructor(private val cheeseService: CheeseService,
            private val networkUtil: NetworkUtil) {

    fun getCheeses(): Single<List<CheeseDto>> {
        return Single.create<List<CheeseDto>> { emitter ->
            try {
                if (networkUtil.isConnected) {
                    val response = cheeseService.getCheeses().execute()
                    if (response.isSuccessful) {
                        emitter.onSuccess(response.body())
                        return@create
                    } else {
                        Timber.e("Failed to load cheeses (%s) : %s", response.code(), response.message())
                    }
                } else {
                    Timber.w("Network not connected")
                }
            } catch (e: Exception) {
                Timber.e(e, "Exception fetching cheese")
            }

            emitter.onSuccess(emptyList<CheeseDto>())
        }
    }

    fun getCheese(cheeseId: Long): Maybe<CheeseDto> {
        return Maybe.create<CheeseDto> { emitter ->
            try {
                if (networkUtil.isConnected) {
                    val response = cheeseService.getCheese(cheeseId).execute()
                    if (response.isSuccessful) {
                        emitter.onSuccess(response.body())
                    } else {
                        Timber.e("Failed to get cheese %d (\$s) : %s", cheeseId, response.code(), response.message())
                    }
                } else {
                    Timber.w("Network not connected")
                }
            } catch (e: Exception) {
                Timber.e(e, "Exception fetching cheese %d", cheeseId)
            }

            emitter.onComplete()
        }
    }
}
