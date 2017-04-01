package com.vikingsen.cheesedemo.ux.cheeselist;


import android.support.annotation.Nullable;

import com.vikingsen.cheesedemo.model.data.cheese.CheeseRepository;
import com.vikingsen.cheesedemo.util.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

class CheeseListPresenter {

    private final CheeseListContract.View view;
    private final CheeseRepository cheeseRepository;
    private final SchedulerProvider schedulerProvider;

    @Nullable
    private Disposable cheeseDisposable = null;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    CheeseListPresenter(CheeseListContract.View view,
                        CheeseRepository cheeseRepository,
                        SchedulerProvider schedulerProvider) {
        this.view = view;
        this.cheeseRepository = cheeseRepository;
        this.schedulerProvider = schedulerProvider;
    }

    void start() {
        loadCheeses(false);
    }

    void stop() {
        compositeDisposable.clear();
    }

    void loadCheeses(boolean forceRefresh) {
        view.showLoading(true);
        if (cheeseDisposable != null) {
            cheeseDisposable.dispose();
            compositeDisposable.remove(cheeseDisposable);
        }
        cheeseDisposable = cheeseRepository.getCheeses(forceRefresh)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(cheeses -> {
                            view.showCheeses(cheeses);
                            view.showLoading(false);
                        },
                        throwable -> {
                            Timber.e(throwable, "Error loading cheeses");
                            view.showError();
                            view.showLoading(false);
                        });
    }
}
