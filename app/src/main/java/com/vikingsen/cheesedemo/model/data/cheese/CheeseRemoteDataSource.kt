package com.vikingsen.cheesedemo.model.data.cheese


import com.vikingsen.cheesedemo.model.webservice.ApiResponse
import com.vikingsen.cheesedemo.model.webservice.CheeseService
import com.vikingsen.cheesedemo.model.webservice.dto.CheeseDto
import com.vikingsen.cheesedemo.util.NetworkDisconnectedException
import com.vikingsen.cheesedemo.util.NetworkUtil
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheeseRemoteDataSource @Inject
constructor(private val cheeseService: CheeseService,
            private val networkUtil: NetworkUtil) {

    fun getCheeses(): ApiResponse<List<CheeseDto>> {
        try {
            if (networkUtil.isConnected()) {
                return ApiResponse(cheeseService.getCheeses().execute())
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception fetching cheese")
        }
        return ApiResponse(NetworkDisconnectedException())
    }

    fun getCheese(cheeseId: Long): ApiResponse<CheeseDto> {
        try {
            if (networkUtil.isConnected()) {
                return ApiResponse(cheeseService.getCheese(cheeseId).execute())
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception fetching cheese")
        }
        return ApiResponse(NetworkDisconnectedException())
    }
}
