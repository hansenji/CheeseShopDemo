package com.vikingsen.cheesedemo.model.data.price

import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PriceRepository @Inject
internal constructor(private val remoteDataSource: PriceRemoteDataSource) {

    fun getPrice(cheeseId: Long, forceRefresh: Boolean): Single<Price> {
        return Single.just(Pair(cheeseId, forceRefresh))
                .flatMap { pair ->

                    // Fake Network time
                    try {
                        TimeUnit.SECONDS.sleep(2)
                    } catch (ignore: Exception) {
                        // Ignore
                    }

                    // Only pull from the network no caching
                    remoteDataSource.getPrice(pair.first)
                            .map { (_, cheeseId, price) -> Price(cheeseId, price) }
                }
    }
}
