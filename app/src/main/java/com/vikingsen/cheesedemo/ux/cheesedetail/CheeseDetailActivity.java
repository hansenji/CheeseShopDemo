package com.vikingsen.cheesedemo.ux.cheesedetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vikingsen.cheesedemo.BuildConfig;
import com.vikingsen.cheesedemo.R;
import com.vikingsen.cheesedemo.inject.Injector;
import com.vikingsen.cheesedemo.model.database.cheese.Cheese;
import com.vikingsen.cheesedemo.util.DrawableUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pocketknife.BindExtra;
import pocketknife.PocketKnife;


public class CheeseDetailActivity extends AppCompatActivity implements CheeseDetailContract.View {

    public static final String EXTRA_CHEESE_ID = "EXTRA_CHEESE_ID";
    public static final String EXTRA_CHEESE_NAME = "EXTRA_CHEESE_NAME";

    @Inject
    CheeseDetailPresenter presenter;

    @BindView(R.id.cdToolbar)
    Toolbar cdToolbar;
    @BindView(R.id.cdCollapsingToolbar)
    CollapsingToolbarLayout cdCollapsingToolbar;
    @BindView(R.id.cdBackdrop)
    ImageView cdBackdropImageView;
    @BindView(R.id.cdRecyclerView)
    RecyclerView cdRecyclerView;
    @BindView(R.id.cdCoordinatorLayout)
    CoordinatorLayout cdCoordinatorLayout;
    @BindView(R.id.cdFab)
    FloatingActionButton cdFab;

    @BindExtra(EXTRA_CHEESE_ID)
    long cheeseId;
    @BindExtra(EXTRA_CHEESE_NAME)
    String cheeseName;

    private CheeseDetailAdapter adapter = new CheeseDetailAdapter();
    private MenuItem commentMenuItem;

    public CheeseDetailActivity() {
        Injector.get()
                .include(new CheeseDetailModule(this))
                .inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheese_detail);
        ButterKnife.bind(this);
        PocketKnife.bindExtras(this);
        presenter.init(cheeseId);

        setSupportActionBar(cdToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        cdCollapsingToolbar.setTitle(cheeseName);

        setupRecyclerView();
        setupFab();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    protected void onStop() {
        presenter.stop();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cheese_detail, menu);
        DrawableUtil.tintAllMenuIcons(menu, ContextCompat.getColor(this, android.R.color.white));
        commentMenuItem = menu.findItem(R.id.menu_item_comment);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_item_comment:
                // TODO: 4/2/17 Add Comment
                break;
            case R.id.menu_item_refresh:
                presenter.loadCheese(true);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    //    @Override
//    public void showLoading(boolean loading) {
//    }

    @Override
    public void showCheese(Cheese cheese) {
        adapter.setCheese(cheese);
        loadBackdrop(cheese.getImageUrl());
        enableComments(true);
    }

    @Override
    public void showError() {
        Snackbar.make(cdCoordinatorLayout, R.string.failed_to_load_cheese, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, v -> presenter.loadCheese(true))
                .show();
        enableComments(false);
        showFab(false);
    }

    @Override
    public void showMissingCheese() {
        Snackbar.make(cdCoordinatorLayout, R.string.unable_to_find_cheese, Snackbar.LENGTH_INDEFINITE).show();
        showFab(false);
        enableComments(false);
    }

    private void setupRecyclerView() {
        cdRecyclerView.setAdapter(adapter);
        cdRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setupFab() {
        cdFab.setOnClickListener(v -> Snackbar.make(cdCoordinatorLayout, R.string.thank_you_for_purchase, Snackbar.LENGTH_SHORT).show());
        showFab(false);
    }

    private void showFab(boolean show) {
        if (show) {
            cdFab.show();
        } else {
            cdFab.hide();
        }
    }

    private void enableComments(boolean enable) {
        commentMenuItem.setEnabled(enable);
    }

    private void loadBackdrop(String imageUrl) {
        Glide.with(this).load(BuildConfig.IMAGE_BASE_URL + imageUrl)
                .centerCrop()
                .into(cdBackdropImageView);
    }
}
