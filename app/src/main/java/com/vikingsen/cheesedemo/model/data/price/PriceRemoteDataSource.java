package com.vikingsen.cheesedemo.model.data.price;


import com.vikingsen.cheesedemo.model.webservice.CheeseService;
import com.vikingsen.cheesedemo.model.webservice.dto.PriceDto;
import com.vikingsen.cheesedemo.util.NetworkDisconnectedException;
import com.vikingsen.cheesedemo.util.NetworkUtil;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import retrofit2.Response;
import timber.log.Timber;

@Singleton
class PriceRemoteDataSource {

    private final CheeseService cheeseService;
    private final NetworkUtil networkUtil;

    @Inject
    PriceRemoteDataSource(CheeseService cheeseService,
                          NetworkUtil networkUtil) {
        this.cheeseService = cheeseService;
        this.networkUtil = networkUtil;
    }

    Single<PriceDto> getPrice(long cheeseId) {
        return Single.create(emitter -> {
            try {
                if (networkUtil.isConnected()) {
                    Response<PriceDto> response = cheeseService.getPrice(cheeseId).execute();
                    if (response.isSuccessful()) {
                        emitter.onSuccess(response.body());
                    } else {
                        Timber.e("Failed to load price for cheese %d (%s) : %s", cheeseId, response.code(), response.message());
                        emitter.onError(new PriceFailureException());
                    }
                } else {
                    Timber.w("Network not connected");
                    emitter.onError(new NetworkDisconnectedException());
                }
            } catch (Exception e) {
                Timber.e(e, "Exception fetching price for cheese %d", cheeseId);
                emitter.onError(new PriceFailureException(e));
            }
        });
    }
}
