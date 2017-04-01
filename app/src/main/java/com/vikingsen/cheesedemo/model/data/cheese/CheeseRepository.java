package com.vikingsen.cheesedemo.model.data.cheese;


import com.vikingsen.cheesedemo.model.database.cheese.Cheese;

import org.dbtools.android.domain.DatabaseTableChange;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import kotlin.Pair;
import timber.log.Timber;

@Singleton
public class CheeseRepository {

    private final CheeseRemoteDataSource remoteDataSource;
    private final CheeseLocalDataSource localDataSource;

    @Inject
    CheeseRepository(CheeseRemoteDataSource remoteDataSource, CheeseLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public Single<List<Cheese>> getCheeses(boolean forceRefresh) {
        return Single.just(forceRefresh)
                .flatMap(force -> {
                    Single<List<Cheese>> remoteCheese = getAndSaveRemoteCheeses();
                    Single<List<Cheese>> localCheese = localDataSource.getCheeses();
                    if (force || localDataSource.isCheeseStale()) {
                        Timber.d("CHEESE IS STALE");
                        return concatSources(remoteCheese, localCheese);
                    } else {
                        Timber.d("CHEESE IS FRESH");
                        return concatSources(localCheese, remoteCheese);
                    }
                });
    }

    public Maybe<Cheese> getCheese(long cheeseId, boolean forceRefresh) {
        return Maybe.just(new Pair<>(cheeseId, forceRefresh))
                .flatMap(pair -> {
                    long id = pair.getFirst();
                    boolean force = pair.getSecond();
                    Maybe<Cheese> remoteCheese = getAndSaveRemoteCheese(id);
                    Maybe<Cheese> localCheese = localDataSource.getCheese(id);
                            if (force || localDataSource.isCheeseStale(id)) {
                                Timber.d("CHEESE IS STALE");
                                return concatSources(remoteCheese, localCheese);
                            } else {
                                Timber.d("CHEESE IS FRESH");
                                return concatSources(localCheese, remoteCheese);
                            }
                        }
                );
    }

    public long modelTs() {
        return localDataSource.modelTs();
    }

    public Observable<DatabaseTableChange> dataChanges() {
        return localDataSource.dataChanges();
    }

    public boolean isUpdatedSince(long modelTs) {
        return modelTs() != modelTs;
    }

    private Single<List<Cheese>> getAndSaveRemoteCheeses() {
        return remoteDataSource.getCheeses().map(cheeses -> localDataSource.saveCheeses(cheeses));
    }

    private Maybe<Cheese> getAndSaveRemoteCheese(Long cheeseId) {
        return remoteDataSource.getCheese(cheeseId).map(cheese -> localDataSource.saveCheese(cheese));
    }

    private Single<List<Cheese>> concatSources(Single<List<Cheese>> source1, Single<List<Cheese>> source2) {
        return Single.concat(source1, source2)
                .filter(list -> !list.isEmpty())
                .first(Collections.emptyList());
    }

    private Maybe<Cheese> concatSources(Maybe<Cheese> source1, Maybe<Cheese> source2) {
        return Maybe.concat(source1, source2)
                .firstElement();
    }
}
