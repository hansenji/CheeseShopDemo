package com.vikingsen.cheesedemo.ux.cheeselist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.devbrackets.android.recyclerext.layoutmanager.AutoColumnGridLayoutManager
import com.vikingsen.cheesedemo.R
import com.vikingsen.cheesedemo.inject.Injector
import com.vikingsen.cheesedemo.intent.InternalIntent
import com.vikingsen.cheesedemo.model.Resource
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import kotlinx.android.synthetic.main.activity_cheese_list.*
import javax.inject.Inject


class CheeseListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var internalIntent: InternalIntent

    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(CheeseListViewModel::class.java) }
    private val adapter by lazy {
        CheeseListAdapter(Glide.with(this)).apply {
            onClickListener = { startActivity(internalIntent.getCheeseDetailIntent(this@CheeseListActivity, it.id, it.name)) }
        }
    }

    init {
        Injector.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cheese_list)

        setSupportActionBar(clToolbar)
        setTitle(R.string.cheese_shop)

        setupRecyclerView()
        setupSwipeRefresh()

        viewModel.cheeses.observe {
            when (it) {
                is Resource.Loading -> onLoading(it.data)
                is Resource.Success -> onSuccess(it.data)
                is Resource.Error -> onError(it.data)
            }
        }
    }

    private fun setupRecyclerView() {
        clRecyclerView.adapter = adapter

        val layoutManager = AutoColumnGridLayoutManager(this, resources.getDimensionPixelSize(R.dimen.grid_item_width))
        layoutManager.setMinColumnSpacing(resources.getDimensionPixelSize(R.dimen.grid_space))
        layoutManager.setMatchRowAndColumnSpacing(true)
        layoutManager.setSpacingMethod(AutoColumnGridLayoutManager.SpacingMethod.EDGES)
        clRecyclerView.layoutManager = layoutManager
    }

    private fun setupSwipeRefresh() {
        clSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark)
        clSwipeRefreshLayout.setOnRefreshListener { viewModel.forceLoad() }
    }

    private fun onLoading(cheeses: List<Cheese>?) {
        clSwipeRefreshLayout.isRefreshing = true
        cheeses?.let {
            adapter.cheeses = it
        }
    }

    private fun onSuccess(cheeses: List<Cheese>?) {
        clSwipeRefreshLayout.isRefreshing = false
        cheeses ?: return onError()
        if (cheeses.isEmpty() && adapter.itemCount > 0) {
            onError()
        } else {
            adapter.cheeses = cheeses
        }
    }

    private fun onError(cheeses: List<Cheese>? = null) {
        clSwipeRefreshLayout.isRefreshing = false
        cheeses?.let {
            adapter.cheeses = it
        }
        Snackbar.make(clCoordinatorLayout, R.string.failed_to_refresh_cheeses, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry) { viewModel.forceLoad() }
                .show()
    }

    private inline fun <T> LiveData<T>.observe(crossinline block: (T?) -> Unit) {
        observe(this@CheeseListActivity, Observer { block(it) })
    }
}
