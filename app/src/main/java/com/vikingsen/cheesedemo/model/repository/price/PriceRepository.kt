package com.vikingsen.cheesedemo.model.repository.price

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.vikingsen.cheesedemo.model.Resource
import com.vikingsen.cheesedemo.model.webservice.ApiResponse
import com.vikingsen.cheesedemo.model.webservice.CheeseService
import com.vikingsen.cheesedemo.model.webservice.dto.PriceDto
import com.vikingsen.cheesedemo.util.NetworkDisconnectedException
import com.vikingsen.cheesedemo.util.NetworkUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class PriceRepository
@Inject constructor(private val remoteDataSource: PriceRemoteDataSource) {
    fun getPrice(cheeseId: Long): LiveData<Resource<Price>> {
        return remoteDataSource.getPrice(cheeseId).map {
            @Suppress("RemoveExplicitTypeArguments", "SENSELESS_COMPARISON") // It is not really senseless livedata can be null
            when {
                it == null -> Resource.Loading<Price>()
                it.successful -> Resource.Success(it.data?.mapToPrice())
                else -> Resource.Error<Price>()
            }
        }
    }

    private fun PriceDto?.mapToPrice(): Price? {
        this ?: return null
        return Price(cheeseId, price)
    }
}

data class Price(val cheeseId: Long, val price: Double)

@Singleton
class PriceRemoteDataSource
@Inject constructor(
    private val cheeseService: CheeseService,
    private val networkUtil: NetworkUtil
) {
    fun getPrice(cheeseId: Long): LiveData<ApiResponse<PriceDto>> {
        return object : LiveData<ApiResponse<PriceDto>>(), CoroutineScope by IOScope() {
            private val executed = AtomicBoolean(false)

            override fun onActive() {
                if (executed.compareAndSet(false, true)) {
                    execute()
                }
            }

            override fun onInactive() {
                if (value == null) {
                    executed.set(false)
                }
                cancel()
                super.onInactive()
            }

            private fun execute() {
                launch {
                    postValue(null)
                    // Fake Network Time
                    delay(TimeUnit.SECONDS.toMillis(2))
                    try {
                        if (networkUtil.isConnected()) {
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

@Suppress("FunctionName")
@ExperimentalCoroutinesApi // Experimental since 1.1.0, tentatively until 1.2.0
fun IOScope(): CoroutineScope = ContextScope(SupervisorJob() + Dispatchers.IO)

internal class ContextScope(context: CoroutineContext) : CoroutineScope {
    override val coroutineContext: CoroutineContext = context
}