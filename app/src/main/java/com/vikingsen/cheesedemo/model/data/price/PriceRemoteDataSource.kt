package com.vikingsen.cheesedemo.model.data.price


import android.arch.lifecycle.LiveData
import com.vikingsen.cheesedemo.model.webservice.ApiResponse
import com.vikingsen.cheesedemo.model.webservice.CheeseService
import com.vikingsen.cheesedemo.model.webservice.dto.PriceDto
import com.vikingsen.cheesedemo.util.CoroutineContextProvider
import com.vikingsen.cheesedemo.util.NetworkDisconnectedException
import com.vikingsen.cheesedemo.util.NetworkUtil
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PriceRemoteDataSource
@Inject constructor(
        private val cheeseService: CheeseService,
        private val networkUtil: NetworkUtil,
        private val ccp: CoroutineContextProvider
) {

    fun getPrice(cheeseId: Long): LiveData<ApiResponse<PriceDto>> {
        return object : LiveData<ApiResponse<PriceDto>>() {
            private val executed = AtomicBoolean(false)

            override fun onActive() {
                if (executed.compareAndSet(false, true)) {
                    execute()
                }
            }

            private fun execute() {
                launch(ccp.commonPool) {
                    postValue(null)
                    // Fake Network Time
                    delay(2, TimeUnit.SECONDS)
                    try {
                        if (networkUtil.isConnected) {
                            postValue(ApiResponse(cheeseService.getPrice(cheeseId).execute()))
                            return@launch
                        }
                    } catch (e: Exception) {
                        Timber.e(e, "Exception fetching cheese")
                    }
                    postValue(ApiResponse(NetworkDisconnectedException()))
                }
            }
        }
    }
}
