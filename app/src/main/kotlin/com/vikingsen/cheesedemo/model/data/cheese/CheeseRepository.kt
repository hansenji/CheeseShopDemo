package com.vikingsen.cheesedemo.model.data.cheese

import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import io.reactivex.Maybe
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CheeseRepository
@Inject constructor(private val remoteDataSource: CheeseRemoteDataSource, private val localDataSource: CheeseLocalDataSource) {

    fun getCheeses(forceRefresh: Boolean): Single<List<Cheese>> {
        return Single.just(forceRefresh)
                .flatMap { force ->
                    val remoteCheese = getAndSaveRemoteCheeses()
                    val localCheese = localDataSource.getCheeses()
                    if (force || localDataSource.isCheeseStale()) {
                        Timber.d("CHEESE IS STALE")
                        return@flatMap concatSources(remoteCheese, localCheese)
                    } else {
                        Timber.d("CHEESE IS FRESH")
                        return@flatMap concatSources(localCheese, remoteCheese)
                    }
                }
    }

    fun getCheese(cheeseId: Long, forceRefresh: Boolean): Maybe<Cheese> {
        return Maybe.just(Pair(cheeseId, forceRefresh))
                .flatMap<Cheese> {
                    val id = it.first
                    val force = it.second

                    val remoteCheese = getAndSaveRemoteCheese(id)
                    val localCheese = localDataSource.getCheese(id)

                    if (force || localDataSource.isCheeseStale(id)) {
                        Timber.d("CHEESE IS STALE")
                        return@flatMap concatSources(remoteCheese, localCheese)
                    } else {
                        Timber.d("CHEESE IS FRESH")
                        return@flatMap concatSources(localCheese, remoteCheese)
                    }
                }
    }

    private fun getAndSaveRemoteCheeses(): Single<List<Cheese>> {
        return remoteDataSource.getCheeses().map { cheeses -> localDataSource.saveCheeses(cheeses) }
    }

    private fun getAndSaveRemoteCheese(cheeseId: Long): Maybe<Cheese> {
        return remoteDataSource.getCheese(cheeseId).map { cheese -> localDataSource.saveCheese(cheese) }
    }

    private fun concatSources(source1: Single<List<Cheese>>, source2: Single<List<Cheese>>): Single<List<Cheese>> {
        return Single.concat(source1, source2)

                // If the source1 returns an empty list use the source2
                .filter { it.isNotEmpty() }

                // Only take the first list and don't execute any other observables
                // or return an empty list if both sources fail
                .first(emptyList<Cheese>())
    }

    private fun concatSources(source1: Maybe<Cheese>, source2: Maybe<Cheese>): Maybe<Cheese> {
        return Maybe.concat(source1, source2)
                .firstElement()
    }
}