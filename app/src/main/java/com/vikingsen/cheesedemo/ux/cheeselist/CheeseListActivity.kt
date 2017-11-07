package com.vikingsen.cheesedemo.ux.cheeselist

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.devbrackets.android.recyclerext.layoutmanager.AutoColumnGridLayoutManager
import com.vikingsen.cheesedemo.R
import com.vikingsen.cheesedemo.inject.Injector
import com.vikingsen.cheesedemo.intent.InternalIntent
import com.vikingsen.cheesedemo.model.room.cheese.Cheese
import kotlinx.android.synthetic.main.activity_cheese_list.*
import javax.inject.Inject


class CheeseListActivity : AppCompatActivity(), CheeseListContract.View {

    @Inject
    lateinit var presenter: CheeseListPresenter
    @Inject
    lateinit var internalIntent: InternalIntent

    private lateinit var adapter: CheeseListAdapter

    init {
        Injector.get()
                .include(CheeseListModule(this))
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cheese_list)

        setSupportActionBar(clToolbar)
        setTitle(R.string.cheese_shop)

        setupRecyclerView()
        setupSwipeRefresh()
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        presenter.stop()
        super.onStop()
    }

    override fun showLoading(loading: Boolean) {
        clSwipeRefreshLayout.isRefreshing = loading
    }

    override fun showCheeses(cheeses: List<Cheese>) {
        if (cheeses.isEmpty() && adapter.itemCount > 0) {
            showError()
        } else {
            adapter.cheeses = cheeses
        }
    }

    override fun showError() {
        Snackbar.make(clCoordinatorLayout, R.string.failed_to_refresh_cheeses, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry) { presenter.loadCheeses(true) }
                .show()
    }

    private fun setupRecyclerView() {
        adapter = CheeseListAdapter(Glide.with(this))
        adapter.onClickListener = { startActivity(internalIntent.getCheeseDetailIntent(this, it.id, it.name)) }

        val resources = resources
        clRecyclerView.adapter = adapter
        val layoutManager = AutoColumnGridLayoutManager(this, resources.getDimensionPixelSize(R.dimen.grid_item_width))
        layoutManager.setMinColumnSpacing(resources.getDimensionPixelSize(R.dimen.grid_space))
        layoutManager.setMatchRowAndColumnSpacing(true)
        layoutManager.setSpacingMethod(AutoColumnGridLayoutManager.SpacingMethod.EDGES)
        clRecyclerView.layoutManager = layoutManager
    }

    private fun setupSwipeRefresh() {
        clSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark)
        clSwipeRefreshLayout.setOnRefreshListener { presenter.loadCheeses(true) }
    }
}
