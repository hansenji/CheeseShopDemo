package com.vikingsen.cheesedemo.model

import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @param R ResultType
 * @param T RequestType
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
abstract class NetworkBoundResource<R, T>
@MainThread constructor() {
    private val resultMediatorLiveData = MediatorLiveData<Resource<R>>()

    init {
        resultMediatorLiveData.value = Resource.Loading()
        @Suppress("LeakingThis") // This is not leaked
        val dbSource = loadFromDb()
        resultMediatorLiveData.addSource(dbSource) { data ->
            resultMediatorLiveData.removeSource(dbSource)
            GlobalScope.launch(Dispatchers.Main) {
                val shouldFetch = withContext(Dispatchers.IO) { shouldFetch(data) }
                if (shouldFetch) {
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
    abstract suspend fun CoroutineScope.shouldFetch(data: R?): Boolean

    @WorkerThread
    abstract suspend fun CoroutineScope.fetchFromNetwork(): NetworkResponse<T>

    @WorkerThread
    abstract suspend fun CoroutineScope.saveNetworkData(data: T)

    @WorkerThread
    protected open fun processNetworkResponse(response: NetworkResponse<T>): T? = response.data

    /**
     * return message id to display to the user, 0 for no/default message
     */
    @MainThread
    @StringRes
    protected open fun getErrorMessageId(error: Throwable?, errorMessage: String?): Int = 0

    @MainThread
    protected open fun onFetchFailed() {

    }

    fun asLiveData(): LiveData<Resource<R>> = resultMediatorLiveData

    @MainThread
    private suspend fun fetchFromNetwork(dbSource: LiveData<R>) = coroutineScope {
        resultMediatorLiveData.postValue(Resource.Loading(dbSource.value))

        val response = withContext(Dispatchers.IO) { fetchFromNetwork() }

        if (response.successful) {
            processNetworkResponse(response)?.let {
                withContext(Dispatchers.IO) { saveNetworkData(it) }
            } ?: return@coroutineScope onError(dbSource, response)

            // we specially request a new live data,
            // otherwise we will get immediately last cached value,
            // which may not be updated with latest results received from network.
            resultMediatorLiveData.addSource(loadFromDb()) {
                resultMediatorLiveData.postValue(Resource.Success(it))
            }
        } else {
            onError(dbSource, response)
        }
    }

    @MainThread
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