package com.vikingsen.cheesedemo.model.data.cheese

import android.arch.lifecycle.LiveData
import com.vikingsen.cheesedemo.model.NetworkBoundResource
import com.vikingsen.cheesedemo.model.Resource
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import com.vikingsen.cheesedemo.model.webservice.dto.CheeseDto
import com.vikingsen.cheesedemo.util.CoroutineContextProvider
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CheeseRepository
@Inject constructor(
        private val remoteDataSource: CheeseRemoteDataSource,
        private val localDataSource: CheeseLocalDataSource,
        private val ccp: CoroutineContextProvider
) {

    fun getCheeses(forceRefresh: Boolean): LiveData<Resource<List<Cheese>>> =
            object : NetworkBoundResource<List<Cheese>, List<CheeseDto>>(ccp.commonPool) {
                override fun loadFromDb() = localDataSource.getCheeses()

                suspend override fun shouldFetch(data: List<Cheese>?): Boolean {
                    if (forceRefresh || data == null) {
                        return true
                    }

                    val cacheExpiration = LocalDateTime.now().minus(CACHE_VALID_AMOUNT, CACHE_VALID_UNIT)
                    // GOTCHA - DOUBLE CHECK THAT THE CHECK MATCHES THE METHOD NAME (isBefore vs isAfter)
                    return data.map { it.cached }.min()?.isBefore(cacheExpiration) ?: true
                }

                suspend override fun fetchFromNetwork(): NetworkResponse<List<CheeseDto>> {
                    return remoteDataSource.getCheeses()
                }

                suspend override fun saveNetworkData(data: List<CheeseDto>) {
                    localDataSource.saveCheeses(data)
                }

            }.asLiveData()

    fun getCheese(cheeseId: Long, forceRefresh: Boolean): LiveData<Resource<Cheese>> =
            object : NetworkBoundResource<Cheese, CheeseDto>(ccp.commonPool) {
                override fun loadFromDb() = localDataSource.getCheese(cheeseId)

                suspend override fun shouldFetch(data: Cheese?): Boolean {
                    if (forceRefresh || data == null) {
                        return true
                    }

                    val cacheExpiration = LocalDateTime.now().minus(CACHE_VALID_AMOUNT, CACHE_VALID_UNIT)
                    // GOTCHA - DOUBLE CHECK THAT THE CHECK MATCHES THE METHOD NAME (isBefore vs isAfter)
                    return data.cached.isBefore(cacheExpiration)
                }

                suspend override fun fetchFromNetwork(): NetworkResponse<CheeseDto> {
                    return remoteDataSource.getCheese(cheeseId)
                }

                suspend override fun saveNetworkData(data: CheeseDto) {
                    localDataSource.saveCheese(data)
                }
            }.asLiveData()

    companion object {
        private val CACHE_VALID_AMOUNT = 1L
        private val CACHE_VALID_UNIT = ChronoUnit.HOURS
    }
}