package com.vikingsen.cheesedemo.model.data.cheese

import android.arch.lifecycle.LiveData
import com.vikingsen.cheesedemo.model.NetworkBoundResource
import com.vikingsen.cheesedemo.model.Resource
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import com.vikingsen.cheesedemo.model.webservice.dto.CheeseDto
import io.reactivex.Maybe
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CheeseRepository
@Inject constructor(private val remoteDataSource: CheeseRemoteDataSource, private val localDataSource: CheeseLocalDataSource) {

    fun getCheeses(forceRefresh: Boolean): LiveData<Resource<List<Cheese>>> = object: NetworkBoundResource<List<Cheese>, List<CheeseDto>>() {
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

    fun getCheese(cheeseId: Long, forceRefresh: Boolean): Maybe<Cheese> {
        return Maybe.just(Pair(cheeseId, forceRefresh))
                .flatMap<Cheese> {
                    val id = it.first
                    val force = it.second

                    val remoteCheese = getAndSaveRemoteCheese(id)
                    val localCheese = localDataSource.getCheese(id)

                    if (force || localDataSource.areCheeseStale(id)) {
                        Timber.d("CHEESE IS STALE")
                        return@flatMap concatSources(remoteCheese, localCheese)
                    } else {
                        Timber.d("CHEESE IS FRESH")
                        return@flatMap concatSources(localCheese, remoteCheese)
                    }
                }
    }

    private fun getAndSaveRemoteCheese(cheeseId: Long): Maybe<Cheese> {
        return remoteDataSource.getCheese(cheeseId).map { cheese -> localDataSource.saveCheese(cheese) }
    }

    private fun concatSources(source1: Maybe<Cheese>, source2: Maybe<Cheese>): Maybe<Cheese> {
        return Maybe.concat(source1, source2)
                .firstElement()
    }

    companion object {
        private val CACHE_VALID_AMOUNT = 1L
        private val CACHE_VALID_UNIT = ChronoUnit.HOURS
    }
}