package com.vikingsen.cheesedemo.model.data.cheese;


import com.vikingsen.cheesedemo.model.webservice.CheeseService;
import com.vikingsen.cheesedemo.model.webservice.dto.CheeseDto;

import java.util.ArrayList;
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

    @Inject
    CheeseRemoteDataSource(CheeseService cheeseService) {
        this.cheeseService = cheeseService;
    }

    Single<List<CheeseDto>> getCheeses() {
        return Single.create(emitter -> {
            try {
                Response<List<CheeseDto>> response = cheeseService.getCheeses().execute();
                if (response.isSuccessful()) {
                    emitter.onSuccess(response.body());
                } else {
                    Timber.e("Failed to load cheeses (%s) : %s", response.code(), response.message());
                    emitter.onSuccess(new ArrayList<>());
                }
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    Maybe<CheeseDto> getCheese(long cheeseId) {
        return Maybe.create(emitter -> {
            try {
                Response<CheeseDto> response = cheeseService.getCheese(cheeseId).execute();
                if (response.isSuccessful()) {
                    emitter.onSuccess(response.body());
                } else {
                    Timber.e("Failed to get cheese %d ($s) : %s", cheeseId, response.code(), response.message());
                    emitter.onComplete();
                }
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}
