package com.vikingsen.cheesedemo.ux.cheesedetail

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.vikingsen.cheesedemo.BuildConfig
import com.vikingsen.cheesedemo.R
import com.vikingsen.cheesedemo.inject.Injector
import com.vikingsen.cheesedemo.model.data.price.Price
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import com.vikingsen.cheesedemo.model.database.comment.Comment
import com.vikingsen.cheesedemo.util.DrawableUtil
import kotlinx.android.synthetic.main.activity_cheese_detail.*
import pocketknife.BindExtra
import pocketknife.PocketKnife
import javax.inject.Inject


class CheeseDetailActivity : AppCompatActivity(), CheeseDetailContract.View, AddCommentDialogFragment.OnTextListener {

    @Inject
    lateinit var presenter: CheeseDetailPresenter

    @BindExtra(EXTRA_CHEESE_ID)
    var cheeseId: Long = 0
    @BindExtra(EXTRA_CHEESE_NAME)
    lateinit var cheeseName: String

    private val adapter = CheeseDetailAdapter()
    private var commentMenuItem: MenuItem? = null

    init {
        Injector.get()
                .include(CheeseDetailModule(this))
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheese_detail)
        PocketKnife.bindExtras(this)
        presenter.init(cheeseId)

        setSupportActionBar(cdToolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        cdCollapsingToolbar.title = cheeseName

        setupRecyclerView()
        setupFab()

        if (savedInstanceState != null) {
            val fragment = supportFragmentManager.findFragmentByTag(AddCommentDialogFragment.TAG)
            if (fragment is AddCommentDialogFragment) {
                fragment.setOnTextListener(this)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        presenter.stop()
        super.onStop()
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
            R.id.menu_item_refresh -> presenter.reload()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun showCheese(cheese: Cheese) {
        adapter.cheese = cheese
        loadBackdrop(cheese.imageUrl)
        supportInvalidateOptionsMenu()
    }

    override fun showCheeseError() {
        Snackbar.make(cdCoordinatorLayout, R.string.failed_to_load_cheese, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry) { presenter.reload() }
                .show()
        supportInvalidateOptionsMenu()
        updateFabVisibility()
    }

    override fun showMissingCheese() {
        Snackbar.make(cdCoordinatorLayout, R.string.unable_to_find_cheese, Snackbar.LENGTH_INDEFINITE).show()
        updateFabVisibility()
        supportInvalidateOptionsMenu()
    }

    override fun showComments(comments: List<Comment>) {
        if (!comments.isEmpty() || adapter.commentCount <= 0) {
            adapter.comments = comments
        }
    }

    override fun showCommentError() {
        // TODO DO SOMETHING
    }

    override fun showPriceLoading(loading: Boolean) {
        adapter.isLoadingPrice = loading
        updateFabVisibility()
    }

    override fun showPrice(price: Price) {
        adapter.price = price
        updateFabVisibility()
    }

    override fun showPriceError(networkDisconnected: Boolean) {
        adapter.price = null
        updateFabVisibility()
    }

    override fun onTextSubmitted(text: CharSequence) {
        presenter.addNewComment(BuildConfig.USER_NAME, text.toString())
    }

    private fun setupRecyclerView() {
        cdRecyclerView.adapter = adapter
        cdRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun setupFab() {
        cdFab.setOnClickListener { Snackbar.make(cdCoordinatorLayout, R.string.thank_you_for_purchase, Snackbar.LENGTH_SHORT).show() }
        updateFabVisibility()
    }

    private fun updateFabVisibility() {
        if (!adapter.isLoadingPrice && adapter.hasPrice()) {
            cdFab.show()
        } else {
            cdFab.hide()
        }
    }

    private fun loadBackdrop(imageUrl: String) {
        Glide.with(this).load(BuildConfig.IMAGE_BASE_URL + imageUrl)
                .centerCrop()
                .into(cdBackdropImageView)
    }

    private fun showNewCommentDialog() {
        val dialogFragment = AddCommentDialogFragment()
        dialogFragment.setOnTextListener(this)
        dialogFragment.show(supportFragmentManager, AddCommentDialogFragment.TAG)
    }

    companion object {
        const val EXTRA_CHEESE_ID = "EXTRA_CHEESE_ID"
        const val EXTRA_CHEESE_NAME = "EXTRA_CHEESE_NAME"
    }
}
