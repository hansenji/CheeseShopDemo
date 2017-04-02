package com.vikingsen.cheesedemo.model.data.cheese;


import com.vikingsen.cheesedemo.model.webservice.CheeseService;
import com.vikingsen.cheesedemo.model.webservice.dto.CheeseDto;
import com.vikingsen.cheesedemo.util.NetworkUtil;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;
import io.reactivex.Single;
import retrofit2.Response;
import timber.log.Timber;

@Singleton
class CheeseRemoteDataSource {

    private final CheeseService cheeseService;
    private final NetworkUtil networkUtil;

    @Inject
    CheeseRemoteDataSource(CheeseService cheeseService,
                           NetworkUtil networkUtil) {
        this.cheeseService = cheeseService;
        this.networkUtil = networkUtil;
    }

    Single<List<CheeseDto>> getCheeses() {
        return Single.create(emitter -> {
            try {
                if (networkUtil.isConnected()) {
                    Response<List<CheeseDto>> response = cheeseService.getCheeses().execute();
                    if (response.isSuccessful()) {
                        emitter.onSuccess(response.body());
                        return;
                    } else {
                        Timber.e("Failed to load cheeses (%s) : %s", response.code(), response.message());
                    }
                } else {
                    Timber.w("Network not connected");
                }
            } catch (Exception e) {
                Timber.e(e, "Exception fetching data");
            }
            emitter.onSuccess(Collections.emptyList());
        });
    }

    Maybe<CheeseDto> getCheese(long cheeseId) {
        return Maybe.create(emitter -> {
            try {
                if (networkUtil.isConnected()) {
                    Response<CheeseDto> response = cheeseService.getCheese(cheeseId).execute();
                    if (response.isSuccessful()) {
                        emitter.onSuccess(response.body());
                    } else {
                        Timber.e("Failed to get cheese %d ($s) : %s", cheeseId, response.code(), response.message());
                    }
                } else {
                    Timber.w("Network not connected");
                }
            } catch (Exception e) {
                Timber.e(e, "Exception fetching data");
            }
            emitter.onComplete();
        });
    }
}
