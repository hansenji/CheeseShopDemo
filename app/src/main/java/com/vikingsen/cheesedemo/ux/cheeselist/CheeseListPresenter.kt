package com.vikingsen.cheesedemo.ux.cheeselist


import com.vikingsen.cheesedemo.model.data.cheese.CheeseRepository
import com.vikingsen.cheesedemo.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

class CheeseListPresenter
@Inject constructor(private val view: CheeseListContract.View,
                    private val cheeseRepository: CheeseRepository,
                    private val schedulerProvider: SchedulerProvider) {

    private var cheeseDisposable: Disposable? = null
    private val compositeDisposable = CompositeDisposable()

    fun start() {
        loadCheeses(false)
    }

    fun stop() {
        compositeDisposable.clear()
    }

    fun loadCheeses(forceRefresh: Boolean) {
        view.showLoading(true)
        cheeseDisposable?.let {
            it.dispose()
            compositeDisposable.remove(it)
        }
        cheeseDisposable = cheeseRepository.getCheeses(forceRefresh)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        {
                            view.showCheeses(it)
                            view.showLoading(false)
                        },
                        {
                            Timber.e(it, "Error loading cheeses")
                            view.showError()
                            view.showLoading(false)
                        }
                )
    }
}
