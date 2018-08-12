package com.vikingsen.cheesedemo.ux.cheeselist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.vikingsen.cheesedemo.R
import com.vikingsen.cheesedemo.databinding.CheeseListActivityBinding
import com.vikingsen.cheesedemo.inject.Injector
import com.vikingsen.cheesedemo.intent.InternalIntent
import com.vikingsen.cheesedemo.model.Resource
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import com.vikingsen.cheesedemo.ui.util.SpaceItemDecoration
import javax.inject.Inject


class CheeseListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var internalIntent: InternalIntent

    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(CheeseListViewModel::class.java) }
    private val adapter by lazy { CheeseListAdapter(viewModel) }
    private lateinit var binding: CheeseListActivityBinding

    init {
        Injector.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.cheese_list_activity)

        setSupportActionBar(binding.toolbar)
        setTitle(R.string.cheese_shop)

        setupRecyclerView()
        setupSwipeRefresh()

        setupViewModelObservers()
    }

    private fun setupViewModelObservers() {
        viewModel.cheeses.observe {
            when (it) {
                is Resource.Loading -> onLoading(it.data)
                is Resource.Success -> onSuccess(it.data)
                is Resource.Error -> onError(it.data)
            }
        }
        viewModel.cheeseSelected.observeNotNull {
            startActivity(internalIntent.getCheeseDetailIntent(this, it.id, it.name))
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(SpaceItemDecoration(recyclerView.context, R.dimen.grid_space))
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark)
            setOnRefreshListener { viewModel.forceLoad() }
        }
    }

    private fun onLoading(cheeses: List<Cheese>?) {
        binding.swipeRefreshLayout.isRefreshing = true
        cheeses?.let {
            adapter.submitList(cheeses)
        }
    }

    private fun onSuccess(cheeses: List<Cheese>?) {
        binding.swipeRefreshLayout.isRefreshing = false
        cheeses ?: return onError()
        if (cheeses.isEmpty() && adapter.itemCount > 0) {
            onError()
        } else {
            adapter.submitList(cheeses)
        }
    }

    private fun onError(cheeses: List<Cheese>? = null) {
        binding.swipeRefreshLayout.isRefreshing = false
        cheeses?.let {
            adapter.submitList(cheeses)
        }
        Snackbar.make(binding.coordinatorLayout, R.string.failed_to_refresh_cheeses, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry) { viewModel.forceLoad() }
                .show()
    }

    private inline fun <T> LiveData<T>.observe(crossinline block: (T?) -> Unit) {
        observe(this@CheeseListActivity, Observer { block(it) })
    }

    private inline fun <T> LiveData<T>.observeNotNull(crossinline block: (T) -> Unit) {
        observe(this@CheeseListActivity, Observer {
            if (it != null) {
                block(it)
            }
        })
    }
}
