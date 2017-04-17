package com.vikingsen.cheesedemo.ux.cheesedetail


import com.vikingsen.cheesedemo.model.data.cheese.CheeseRepository
import com.vikingsen.cheesedemo.model.data.comment.CommentRepository
import com.vikingsen.cheesedemo.model.data.price.PriceRepository
import com.vikingsen.cheesedemo.util.NetworkDisconnectedException
import com.vikingsen.cheesedemo.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

class CheeseDetailPresenter
@Inject constructor(private val view: CheeseDetailContract.View,
                    private val cheeseRepository: CheeseRepository,
                    private val commentRepository: CommentRepository,
                    private val priceRepository: PriceRepository,
                    private val schedulerProvider: SchedulerProvider) {

    private var cheeseId: Long = -1

    private var cheeseDisposable: Disposable? = null
    private var commentDisposable: Disposable? = null
    private var priceDisposable: Disposable? = null
    private val compositeDisposable = CompositeDisposable()

    fun init(cheeseId: Long) {
        this.cheeseId = cheeseId
    }

    fun start() {
        loadCheese(false)
        loadComments(false)
        loadPrice(false)
        val disposable = commentRepository.modelChanges()
                .observeOn(schedulerProvider.ui())
                .subscribe { modelChange ->
                    if (modelChange.isBulkChange || modelChange.cheeseId == -1L || modelChange.cheeseId == cheeseId) {
                        loadComments(false)
                    }
                }
        compositeDisposable.add(disposable)
    }

    fun stop() {
        compositeDisposable.clear()
    }

    fun reload() {
        loadCheese(true)
        loadComments(true)
        loadPrice(true)
    }

    fun addNewComment(user: String, comment: String) {
        commentRepository.addComment(cheeseId, user, comment)
    }

    private fun loadCheese(forceRefresh: Boolean) {
        if (cheeseId == -1L) {
            throw UninitializedPropertyAccessException("You must call init before calling loadCheese()")
        }
        cheeseDisposable?.let {
            it.dispose()
            compositeDisposable.remove(it)
        }
        cheeseDisposable = cheeseRepository.getCheese(cheeseId, forceRefresh)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        { cheese -> view.showCheese(cheese) },
                        { throwable ->
                            Timber.e(throwable, "Failed to load cheese %d", cheeseId)
                            view.showCheeseError()
                        },
                        {
                            Timber.w("No cheese found for id %d", cheeseId)
                            view.showMissingCheese()
                        }
                )
    }

    private fun loadComments(forceRefresh: Boolean) {
        if (cheeseId == -1L) {
            throw UninitializedPropertyAccessException("You must call init before calling loadCheese()")
        }
        commentDisposable?.let {
            it.dispose()
            compositeDisposable.remove(it)
        }
        commentDisposable = commentRepository.getComments(cheeseId, forceRefresh)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        { comments -> view.showComments(comments) },
                        { throwable ->
                            Timber.e(throwable, "Failed to load comments $%d", cheeseId)
                            view.showCommentError()
                        }
                )
    }

    private fun loadPrice(forceRefresh: Boolean) {
        if (cheeseId == -1L) {
            throw UninitializedPropertyAccessException("You must call init before calling loadCheese()")
        }
        priceDisposable?.let {
            it.dispose()
            compositeDisposable.remove(it)
        }
        view.showPriceLoading(true)
        priceDisposable = priceRepository.getPrice(cheeseId, forceRefresh)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        { price ->
                            view.showPrice(price)
                            view.showPriceLoading(false)
                        },
                        { throwable ->
                            Timber.e(throwable, "Failed to load price $%d", cheeseId)
                            view.showPriceError(throwable is NetworkDisconnectedException)
                            view.showPriceLoading(false)
                        }
                )
    }
}
