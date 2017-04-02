package com.vikingsen.cheesedemo.model.data.price;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import kotlin.Pair;

@Singleton
public class PriceRepository {

    private final PriceRemoteDataSource remoteDataSource;

    @Inject
    PriceRepository(PriceRemoteDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public Single<Price> getPrice(long cheeseId, boolean forceRefresh) {
        return Single.just(new Pair<>(cheeseId, forceRefresh))
                .flatMap(pair -> {

                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (Exception ignore) {
                        // Ignore
                    }

                    long id = pair.getFirst();
                    return remoteDataSource.getPrice(id).map(priceDto -> new Price(priceDto.getCheeseId(), priceDto.getPrice()));
                });
    }
}
