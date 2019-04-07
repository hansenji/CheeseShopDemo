package com.vikingsen.cheesedemo.model.repository.cheese

import androidx.lifecycle.LiveData
import com.vikingsen.cheesedemo.model.NetworkBoundResource
import com.vikingsen.cheesedemo.model.Resource
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import com.vikingsen.cheesedemo.model.webservice.dto.CheeseDto
import kotlinx.coroutines.CoroutineScope
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheeseRepository
@Inject constructor(
    private val remoteDataSource: CheeseRemoteDataSource,
    private val localDataSource: CheeseLocalDataSource
) {

    fun getCheeses(forceRefresh: Boolean): LiveData<Resource<List<Cheese>>> =
        object : NetworkBoundResource<List<Cheese>, List<CheeseDto>>() {
            override fun loadFromDb() = localDataSource.getCheeses()

            override suspend fun CoroutineScope.shouldFetch(data: List<Cheese>?): Boolean {
                if (forceRefresh || data == null) {
                    return true
                }

                val cacheExpiration = LocalDateTime.now().minus(
                    CACHE_VALID_AMOUNT,
                    CACHE_VALID_UNIT
                )
                // GOTCHA - DOUBLE CHECK THAT THE CHECK MATCHES THE METHOD NAME (isBefore vs isAfter)
                return data.map { it.cached }.min()?.isBefore(cacheExpiration) ?: true
            }

            override suspend fun CoroutineScope.fetchFromNetwork() = remoteDataSource.getCheeses()

            override suspend fun CoroutineScope.saveNetworkData(data: List<CheeseDto>) {
                localDataSource.saveCheeses(data)
            }

        }.asLiveData()

    fun getCheese(cheeseId: Long, forceRefresh: Boolean): LiveData<Resource<Cheese>> =
        object: NetworkBoundResource<Cheese, CheeseDto>() {
            override fun loadFromDb() = localDataSource.getCheese(cheeseId)

            override suspend fun CoroutineScope.shouldFetch(data: Cheese?): Boolean {
                if (forceRefresh || data == null) {
                    return true
                }

                val cacheExpiration = LocalDateTime.now().minus(
                    CACHE_VALID_AMOUNT,
                    CACHE_VALID_UNIT
                )
                // GOTCHA - DOUBLE CHECK THAT THE CHECK MATCHES THE METHOD NAME (isBefore vs isAfter)
                return data.cached.isBefore(cacheExpiration)
            }

            override suspend fun CoroutineScope.fetchFromNetwork() = remoteDataSource.getCheese(cheeseId)

            override suspend fun CoroutineScope.saveNetworkData(data: CheeseDto) {
                localDataSource.saveCheese(data)
            }

        }.asLiveData()

    companion object {
        private const val CACHE_VALID_AMOUNT = 1L
        private val CACHE_VALID_UNIT = ChronoUnit.HOURS
    }
}