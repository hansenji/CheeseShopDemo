package com.vikingsen.cheesedemo.ux.cheesedetail;


import android.support.annotation.Nullable;

import com.vikingsen.cheesedemo.model.data.cheese.CheeseRepository;
import com.vikingsen.cheesedemo.util.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

class CheeseDetailPresenter {

    private final CheeseDetailContract.View view;
    private final CheeseRepository cheeseRepository;
    private final SchedulerProvider schedulerProvider;

    private long cheeseId = -1;

    @Nullable
    private Disposable cheeseDisposable = null;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    CheeseDetailPresenter(CheeseDetailContract.View view,
                          CheeseRepository cheeseRepository,
                          SchedulerProvider schedulerProvider) {
        this.view = view;
        this.cheeseRepository = cheeseRepository;
        this.schedulerProvider = schedulerProvider;
    }

    void init(long cheeseId) {
        this.cheeseId = cheeseId;
    }

    void start() {
        loadCheese(false);
    }

    void stop() {
        compositeDisposable.clear();
    }

    void loadCheese(boolean forceRefresh) {
        if (cheeseId == -1L) {
            throw new IllegalStateException("You must call init before calling loadCheese()");
        }
//        view.showLoading(true);
        if (cheeseDisposable != null) {
            cheeseDisposable.dispose();
            compositeDisposable.remove(cheeseDisposable);
        }
        cheeseDisposable = cheeseRepository.getCheese(cheeseId, forceRefresh)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        cheese -> {
                            view.showCheese(cheese);
//                            view.showLoading(false);
                        },
                        throwable -> {
                            Timber.e(throwable, "Failed to load cheese %d", cheeseId);
                            view.showError();
//                            view.showLoading(false);
                        },
                        () -> {
                            Timber.w("No cheese found for id %d", cheeseId);
                            view.showMissingCheese();
                        });
    }
}
