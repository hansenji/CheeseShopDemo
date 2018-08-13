package com.vikingsen.cheesedemo.ux.cheesedetail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.vikingsen.cheesedemo.BuildConfig
import com.vikingsen.cheesedemo.R
import com.vikingsen.cheesedemo.databinding.CheeseDetailActivityBinding
import com.vikingsen.cheesedemo.inject.Injector
import com.vikingsen.cheesedemo.model.Resource
import com.vikingsen.cheesedemo.model.data.price.Price
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import com.vikingsen.cheesedemo.model.database.comment.Comment
import com.vikingsen.cheesedemo.util.DrawableUtil
import pocketknife.BindExtra
import pocketknife.PocketKnife
import javax.inject.Inject


class CheeseDetailActivity : AppCompatActivity(), AddCommentDialogFragment.OnTextListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    @BindExtra(EXTRA_CHEESE_ID)
    var cheeseId: Long = 0
    @BindExtra(EXTRA_CHEESE_NAME)
    lateinit var cheeseName: String

    private val adapter = CheeseDetailAdapter()
    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(CheeseDetailViewModel::class.java) }
    private var commentMenuItem: MenuItem? = null

    private lateinit var binding: CheeseDetailActivityBinding

    init {
        Injector.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.cheese_detail_activity)
        PocketKnife.bindExtras(this)

        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        binding.collapsingToolbar.title = cheeseName

        setupRecyclerView()
        setupFab()

        if (savedInstanceState != null) {
            val fragment = supportFragmentManager.findFragmentByTag(AddCommentDialogFragment.TAG)
            if (fragment is AddCommentDialogFragment) {
                fragment.setOnTextListener(this)
            }
        }

        setupObservers()

        viewModel.setCheeseId(cheeseId)
    }

    private fun setupObservers() {
        viewModel.cheese.observeNotNull {
            when (it) {
                is Resource.Success -> onCheeseSuccess(it)
                is Resource.Error -> onCheeseError(it)
            }
        }
        viewModel.comments.observeNotNull {
            when (it) {
                is Resource.Success -> onCommentsSuccess(it)
                is Resource.Error -> onCommentsError(it)
            }
        }
        viewModel.price.observeNotNull {
            when (it) {
                is Resource.Loading -> onPriceLoading()
                is Resource.Success -> onPriceSuccess(it)
                is Resource.Error -> onPriceError()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.cheese_detail, menu)
        DrawableUtil.tintAllMenuIcons(menu, ContextCompat.getColor(this, android.R.color.white))
        commentMenuItem = menu.findItem(R.id.menu_item_comment)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        commentMenuItem?.isEnabled = adapter.hasCheese()
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_item_comment -> showNewCommentDialog()
            R.id.menu_item_refresh -> viewModel.reload()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onTextSubmitted(text: CharSequence) {
        viewModel.addNewComment(BuildConfig.USER_NAME, text.toString())
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun setupFab() {
        binding.fab.setOnClickListener { Snackbar.make(binding.coordinatorLayout, R.string.thank_you_for_purchase, Snackbar.LENGTH_SHORT).show() }
        updateFabVisibility()
    }

    private fun updateFabVisibility() {
        if (!adapter.isLoadingPrice && adapter.hasPrice()) {
            binding.fab.show()
        } else {
            binding.fab.hide()
        }
    }

    private fun showNewCommentDialog() {
        val dialogFragment = AddCommentDialogFragment()
        dialogFragment.setOnTextListener(this)
        dialogFragment.show(supportFragmentManager, AddCommentDialogFragment.TAG)
    }

    private fun onCheeseSuccess(resource: Resource.Success<Cheese>) {
        val cheese = resource.data ?: return showMissingCheese()
        adapter.cheese = cheese
        binding.cheese = cheese
        invalidateOptionsMenu()
    }

    private fun onCheeseError(resource: Resource.Error<Cheese>) {
        resource.data?.let {
            adapter.cheese = it
        }
        Snackbar.make(binding.coordinatorLayout, R.string.failed_to_load_cheese, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry) { viewModel.reload() }
                .show()
        invalidateOptionsMenu()
        updateFabVisibility()
    }

    private fun showMissingCheese() {
        Snackbar.make(binding.coordinatorLayout, R.string.unable_to_find_cheese, Snackbar.LENGTH_INDEFINITE).show()
        updateFabVisibility()
        invalidateOptionsMenu()
    }

    private fun onCommentsSuccess(resource: Resource.Success<List<Comment>>) {
        resource.data?.let {
            if (it.isNotEmpty() || adapter.commentCount <= 0) {
                adapter.comments = it
            }
        }
    }

    private fun onCommentsError(resource: Resource.Error<List<Comment>>) {
        resource.data?.let {
            if (it.isNotEmpty() || adapter.commentCount <= 0) {
                adapter.comments = it
            }
        }

        // Show Some Error
    }

    private fun onPriceLoading() {
        adapter.isLoadingPrice = true
        updateFabVisibility()
    }

    private fun onPriceSuccess(resource: Resource.Success<Price>) {
        adapter.isLoadingPrice = false
        adapter.price = resource.data
        updateFabVisibility()
    }

    private fun onPriceError() {
        adapter.isLoadingPrice = false
        adapter.price = null
        updateFabVisibility()
    }

    private inline fun <T> LiveData<T>.observeNotNull(crossinline block: (T?) -> Unit) {
        observe(this@CheeseDetailActivity, Observer {
            it ?: return@Observer
            block(it)
        })
    }

    companion object {
        const val EXTRA_CHEESE_ID = "EXTRA_CHEESE_ID"
        const val EXTRA_CHEESE_NAME = "EXTRA_CHEESE_NAME"
    }
}
