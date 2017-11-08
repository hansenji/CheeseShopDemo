package com.vikingsen.cheesedemo.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.StringRes
import android.support.annotation.WorkerThread
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.CoroutineContext


@Suppress("MemberVisibilityCanPrivate")
abstract class NetworkBoundResource<R, T>
@MainThread
constructor(protected val coroutineContext: CoroutineContext = CommonPool) {
    private val resultMediatorLiveData = MediatorLiveData<Resource<R>>()

    init {
        resultMediatorLiveData.value = Resource.Loading()
        @Suppress("LeakingThis") // This is not leaked
        val dbSource = loadFromDb()
        resultMediatorLiveData.addSource(dbSource) { data ->
            resultMediatorLiveData.removeSource(dbSource)
            launch(coroutineContext) {
                if (shouldFetch(data)) {
                    fetchFromNetwork(dbSource)
                } else {
                    resultMediatorLiveData.addSource(dbSource) {
                        resultMediatorLiveData.postValue(Resource.Success(it))
                    }
                }
            }
        }
    }

    @MainThread
    abstract fun loadFromDb(): LiveData<R>

    @WorkerThread
    suspend abstract fun shouldFetch(data: R?): Boolean

    @WorkerThread
    suspend abstract fun fetchFromNetwork(): NetworkResponse<T>

    @WorkerThread
    suspend abstract fun saveNetworkData(data: T)

    @WorkerThread
    protected open fun processNetworkResponse(response: NetworkResponse<T>): T? {
        return response.data
    }

    /**
     * return message id to display to the user, 0 for no/default message
     */
    @MainThread
    @StringRes
    protected open fun getErrorMessageId(error: Throwable?, errorMessage: String?): Int {
        return 0
    }

    @MainThread
    protected open fun onFetchFailed() {

    }

    fun asLiveData(): LiveData<Resource<R>> = resultMediatorLiveData

    private suspend fun fetchFromNetwork(dbSource: LiveData<R>) {
        resultMediatorLiveData.postValue(Resource.Loading(dbSource.value))

        val response = fetchFromNetwork()

        if (response.successful) {
            processNetworkResponse(response)?.let {
                saveNetworkData(it)
            } ?: return onError(dbSource, response)

            // we specially request a new live data,
            // otherwise we will get immediately last cached value,
            // which may not be updated with latest results received from network.
            resultMediatorLiveData.addSource(loadFromDb()) {
                resultMediatorLiveData.value = Resource.Success(it)
            }
        } else {
            onError(dbSource, response)
        }
    }

    private fun onError(dbSource: LiveData<R>, response: NetworkResponse<T>? = null) {
        onFetchFailed()
        resultMediatorLiveData.addSource(dbSource) {
            resultMediatorLiveData.postValue(Resource.Error(getErrorMessageId(response?.error, response?.errorResponse), it))
        }
    }

    interface NetworkResponse<out T> {
        val successful: Boolean
        val data: T?
        val error: Throwable?
        val errorResponse: String?
    }
}