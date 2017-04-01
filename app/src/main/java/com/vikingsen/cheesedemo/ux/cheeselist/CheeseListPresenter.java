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
    private long modelTs = 0;

    @Inject
    CheeseListPresenter(CheeseListContract.View view,
                        CheeseRepository cheeseRepository,
                        SchedulerProvider schedulerProvider) {
        this.view = view;
        this.cheeseRepository = cheeseRepository;
        this.schedulerProvider = schedulerProvider;
    }

    void start() {
        Disposable disposable = cheeseRepository.dataChanges()
                .observeOn(schedulerProvider.ui())
                .subscribe(change -> loadCheeses());
        compositeDisposable.add(disposable);
    }

    void stop() {
        compositeDisposable.clear();
    }

    void loadCheeses() {
        view.showLoading(true);
        if (cheeseDisposable != null) {
            cheeseDisposable.dispose();
            compositeDisposable.remove(cheeseDisposable);
        }
        modelTs = cheeseRepository.modelTs();
        cheeseDisposable = cheeseRepository.getCheeses()
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

    void reloadCheeses() {
        if (cheeseRepository.isUpdatedSince(modelTs)) {
            loadCheeses();
        }
    }
}
