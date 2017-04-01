package com.vikingsen.cheesedemo.ux.cheeselist;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.devbrackets.android.recyclerext.layoutmanager.AutoColumnGridLayoutManager;
import com.vikingsen.cheesedemo.R;
import com.vikingsen.cheesedemo.inject.Injector;
import com.vikingsen.cheesedemo.model.database.cheese.Cheese;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CheeseListActivity extends AppCompatActivity implements CheeseListContract.View {

    @Inject
    CheeseListPresenter presenter;

    @BindView(R.id.clToolbar)
    Toolbar clToolbar;
    @BindView(R.id.clCoordinatorLayout)
    CoordinatorLayout clCoordinatorLayout;
    @BindView(R.id.clSwipeRefreshLayout)
    SwipeRefreshLayout clSwipeRefreshLayout;
    @BindView(R.id.clRecyclerView)
    RecyclerView clRecyclerView;

    private CheeseListAdapter adapter;

    public CheeseListActivity() {
        Injector.get()
                .include(new CheeseListModule(this))
                .inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cheese_list);
        ButterKnife.bind(this);

        setSupportActionBar(clToolbar);
        setTitle(R.string.cheese_shop);

        setupRecyclerView();
        setupSwipeRefresh();

        presenter.loadCheeses();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.start();
        presenter.reloadCheeses();
    }

    @Override
    protected void onStop() {
        presenter.stop();
        super.onStop();
    }

    @Override
    public void showLoading(boolean loading) {
        clSwipeRefreshLayout.setRefreshing(loading);
    }

    @Override
    public void showCheeses(List<Cheese> cheeses) {
        if (cheeses.isEmpty() && adapter.getItemCount() > 0) {
            showError();
        } else {
            adapter.setCheeseList(cheeses);
        }
    }

    @Override
    public void showError() {
        Snackbar.make(clCoordinatorLayout, R.string.failed_to_refresh_cheeses, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, v -> presenter.loadCheeses())
                .show();
    }

    private void setupRecyclerView() {
        adapter = new CheeseListAdapter(Glide.with(this));
        adapter.setOnClickListener(cheese -> {
            // TODO: 4/1/17 Add onClickListener
        });

        Resources resources = getResources();
        clRecyclerView.setAdapter(adapter);
        AutoColumnGridLayoutManager layoutManager = new AutoColumnGridLayoutManager(this, resources.getDimensionPixelSize(R.dimen.grid_item_width));
        layoutManager.setMinColumnSpacing(resources.getDimensionPixelSize(R.dimen.grid_space));
        layoutManager.setMatchRowAndColumnSpacing(true);
        layoutManager.setSpacingMethod(AutoColumnGridLayoutManager.SpacingMethod.EDGES);
        clRecyclerView.setLayoutManager(layoutManager);
    }

    private void setupSwipeRefresh() {
        clSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        clSwipeRefreshLayout.setOnRefreshListener(() -> presenter.loadCheeses());
    }
}
