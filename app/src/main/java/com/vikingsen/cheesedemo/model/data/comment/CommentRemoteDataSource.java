package com.vikingsen.cheesedemo.model.data.comment;

import com.vikingsen.cheesedemo.model.webservice.CheeseService;
import com.vikingsen.cheesedemo.model.webservice.dto.CommentDto;
import com.vikingsen.cheesedemo.util.NetworkUtil;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import retrofit2.Response;
import timber.log.Timber;

@Singleton
class CommentRemoteDataSource {

    private final CheeseService cheeseService;
    private final NetworkUtil networkUtil;

    @Inject
    CommentRemoteDataSource(CheeseService cheeseService,
                            NetworkUtil networkUtil) {
        this.cheeseService = cheeseService;
        this.networkUtil = networkUtil;
    }

    Single<List<CommentDto>> getComments(long cheeseId) {
        return Single.create(emitter -> {
            try {
                if (networkUtil.isConnected()) {
                    Response<List<CommentDto>> response = cheeseService.getComments(cheeseId).execute();
                    if (response.isSuccessful()) {
                        emitter.onSuccess(response.body());
                        return;
                    } else {
                        Timber.e("Failed to load comments for cheese %d (%s) : %s", cheeseId, response.code(), response.message());
                    }
                } else {
                    Timber.w("Network not connected");
                }
            } catch (Exception e) {
                Timber.e(e, "Exception fetching comments for cheese %d", cheeseId);
            }
            emitter.onSuccess(Collections.emptyList());
        });
    }
}
