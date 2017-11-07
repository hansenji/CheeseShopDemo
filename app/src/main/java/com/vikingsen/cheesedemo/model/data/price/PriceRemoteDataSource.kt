package com.vikingsen.cheesedemo.model.data.price


import com.vikingsen.cheesedemo.model.webservice.CheeseService
import com.vikingsen.cheesedemo.model.webservice.dto.PriceDto
import com.vikingsen.cheesedemo.util.NetworkDisconnectedException
import com.vikingsen.cheesedemo.util.NetworkUtil
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PriceRemoteDataSource
@Inject constructor(private val cheeseService: CheeseService,
                    private val networkUtil: NetworkUtil) {

    fun getPrice(cheeseId: Long): Single<PriceDto> {
        return Single.create<PriceDto> { emitter ->
            try {
                if (networkUtil.isConnected) {
                    val response = cheeseService.getPrice(cheeseId).execute()
                    if (response.isSuccessful) {
                        val priceDto = response.body()
                        when (priceDto) {
                            null -> emitter.onError(PriceFailureException())
                            else -> emitter.onSuccess(priceDto)
                        }
                    } else {
                        Timber.e("Failed to load price for cheese %d (%s) : %s", cheeseId, response.code(), response.message())
                        emitter.onError(PriceFailureException())
                    }
                } else {
                    Timber.w("Network not connected")
                    emitter.onError(NetworkDisconnectedException())
                }
            } catch (e: Exception) {
                Timber.e(e, "Exception fetching price for cheese %d", cheeseId)
                emitter.onError(PriceFailureException(e))
            }
        }
    }
}
