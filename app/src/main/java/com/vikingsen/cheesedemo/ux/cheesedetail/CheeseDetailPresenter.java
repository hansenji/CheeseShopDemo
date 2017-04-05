package com.vikingsen.cheesedemo.ux.cheesedetail;


import android.support.annotation.Nullable;

import com.vikingsen.cheesedemo.model.data.cheese.CheeseRepository;
import com.vikingsen.cheesedemo.model.data.comment.CommentRepository;
import com.vikingsen.cheesedemo.model.data.price.PriceRepository;
import com.vikingsen.cheesedemo.util.NetworkDisconnectedException;
import com.vikingsen.cheesedemo.util.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

class CheeseDetailPresenter {

    private final CheeseDetailContract.View view;
    private final CheeseRepository cheeseRepository;
    private final CommentRepository commentRepository;
    private final PriceRepository priceRepository;
    private final SchedulerProvider schedulerProvider;

    private long cheeseId = -1;

    @Nullable
    private Disposable cheeseDisposable = null;
    private Disposable commentDisposable = null;
    private Disposable priceDisposable = null;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    CheeseDetailPresenter(CheeseDetailContract.View view,
                          CheeseRepository cheeseRepository,
                          CommentRepository commentRepository,
                          PriceRepository priceRepository,
                          SchedulerProvider schedulerProvider) {
        this.view = view;
        this.cheeseRepository = cheeseRepository;
        this.commentRepository = commentRepository;
        this.priceRepository = priceRepository;
        this.schedulerProvider = schedulerProvider;
    }

    void init(long cheeseId) {
        this.cheeseId = cheeseId;
    }

    void start() {
        loadCheese(false);
        loadComments(false);
        loadPrice(false);
        Disposable disposable = commentRepository.modelChanges()
                .observeOn(schedulerProvider.ui())
                .subscribe(modelChange -> {
                    if (modelChange.isBulkChange() || modelChange.getCheeseId() == -1 || modelChange.getCheeseId() == cheeseId) {
                        loadComments(false);
                    }
                });
        compositeDisposable.add(disposable);
    }

    void stop() {
        compositeDisposable.clear();
    }

    void reload() {
        loadCheese(true);
        loadComments(true);
        loadPrice(true);
    }

    private void loadCheese(boolean forceRefresh) {
        if (cheeseId == -1L) {
            throw new IllegalStateException("You must call init before calling loadCheese()");
        }
        if (cheeseDisposable != null) {
            cheeseDisposable.dispose();
            compositeDisposable.remove(cheeseDisposable);
        }
        cheeseDisposable = cheeseRepository.getCheese(cheeseId, forceRefresh)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        cheese -> view.showCheese(cheese),
                        throwable -> {
                            Timber.e(throwable, "Failed to load cheese %d", cheeseId);
                            view.showCheeseError();
                        },
                        () -> {
                            Timber.w("No cheese found for id %d", cheeseId);
                            view.showMissingCheese();
                        });
    }

    private void loadComments(boolean forceRefresh) {
        if (cheeseId == -1L) {
            throw new IllegalStateException("You must call init before calling loadCheese()");
        }
        if (commentDisposable != null) {
            commentDisposable.dispose();
            compositeDisposable.remove(commentDisposable);
        }
        commentDisposable = commentRepository.getComments(cheeseId, forceRefresh)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        comments -> view.showComments(comments),
                        throwable -> {
                            Timber.e(throwable, "Failed to load comments $%d", cheeseId);
                            view.showCommentError();
                        }
                );
    }

    private void loadPrice(boolean forceRefresh) {
        if (cheeseId == -1L) {
            throw new IllegalStateException("You must call init before calling loadCheese()");
        }
        if (priceDisposable != null) {
            priceDisposable.dispose();
            compositeDisposable.remove(priceDisposable);
        }
        view.showPriceLoading(true);
        priceDisposable = priceRepository.getPrice(cheeseId, forceRefresh)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        price -> {
                            view.showPrice(price);
                            view.showPriceLoading(false);
                        },
                        throwable -> {
                            Timber.e(throwable, "Failed to load price $%d", cheeseId);
                            view.showPriceError(throwable instanceof NetworkDisconnectedException);
                            view.showPriceLoading(false);
                        }
                );
    }
}
