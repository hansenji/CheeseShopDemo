package com.vikingsen.cheesedemo.ux.cheesedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.squareup.inject.assisted.Assisted
import com.vikingsen.cheesedemo.R
import com.vikingsen.cheesedemo.databinding.CheeseDetailFragmentBinding
import com.vikingsen.cheesedemo.inject.Injector
import com.vikingsen.cheesedemo.model.AbsentLiveData
import com.vikingsen.cheesedemo.model.Resource
import com.vikingsen.cheesedemo.model.SingleLiveEvent
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import com.vikingsen.cheesedemo.model.database.comment.Comment
import com.vikingsen.cheesedemo.model.repository.cheese.CheeseRepository
import com.vikingsen.cheesedemo.model.repository.comment.CommentRepository
import com.vikingsen.cheesedemo.model.repository.price.Price
import com.vikingsen.cheesedemo.model.repository.price.PriceRepository
import com.vikingsen.cheesedemo.ui.livedata.LiveDataObserverFragment
import com.vikingsen.cheesedemo.util.DrawableUtil
import com.vikingsen.inject.viewmodel.ViewModelInject
import com.vikingsen.inject.viewmodel.savedstate.SavedStateViewModelFactory
import javax.inject.Inject

class CheeseDetailFragment : LiveDataObserverFragment() {

    @Inject
    lateinit var savedStateViewModelFactoryFactory: SavedStateViewModelFactory.Factory

    private lateinit var binding: CheeseDetailFragmentBinding

    private val viewModel by viewModels<CheeseDetailViewModel> { savedStateViewModelFactoryFactory.create(this, arguments) }
    private val adapter by lazy { CheeseDetailAdapter() }

    init {
        Injector.get().inject(this)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.cheese_detail_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark)
            setOnRefreshListener { viewModel.forceLoad() }
        }
        binding.fab.setOnClickListener { viewModel.buy() }
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(binding.recyclerView.context)
        binding.recyclerView.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupLiveDataObservers()
    }

    private fun setupLiveDataObservers() {
        viewModel.cheese.observeNotNull {
            when (it) {
                is Resource.Loading -> onCheeseLoading(it.data)
                is Resource.Success -> onCheeseSuccess(it.data)
                is Resource.Error -> onCheeseError(it.data)
            }
        }
        viewModel.price.observeNotNull {
            when (it) {
                is Resource.Loading -> onPriceLoading()
                is Resource.Success -> onPriceSuccess(it)
                is Resource.Error -> onPriceError()
            }
        }
        viewModel.comments.observeNotNull {
            when (it) {
                is Resource.Success -> onCommentsSuccess(it)
                is Resource.Error -> onCommentsError(it)
            }
        }

        viewModel.showBuyMessageEvent.observe {
            Snackbar.make(binding.coordinatorLayout, R.string.thank_you_for_purchase, Snackbar.LENGTH_SHORT).show()
        }

        viewModel.showNewCommentEvent.observe {
            NewCommentDialogFragment().show(childFragmentManager, null)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.cheese_detail, menu)
        DrawableUtil.tintAllMenuIcons(menu, ContextCompat.getColor(requireContext(), android.R.color.white))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.new_comment -> viewModel.onNewCommentClicked()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun onCheeseLoading(cheese: Cheese?) {
        binding.swipeRefreshLayout.isRefreshing = true
        adapter.cheese = cheese
    }

    private fun onCheeseSuccess(cheese: Cheese?) {
        binding.swipeRefreshLayout.isRefreshing = false
        adapter.cheese = cheese ?: return showMissingCheese()
    }

    private fun showMissingCheese() {
        Snackbar.make(binding.coordinatorLayout, R.string.unable_to_find_cheese, Snackbar.LENGTH_INDEFINITE).show()
        updateFabVisibility()
    }

    private fun onCheeseError(cheese: Cheese? = null) {
        binding.swipeRefreshLayout.isRefreshing = false
        cheese?.let {
            adapter.cheese = it
        }
        Snackbar.make(binding.coordinatorLayout, R.string.failed_to_load_cheese, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.retry) { viewModel.forceLoad() }
            .show()
        updateFabVisibility()
    }

    private fun onPriceLoading() {
        adapter.isLoadingPrice = true
        adapter.price = null
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

    private fun updateFabVisibility() {
        if (!adapter.isLoadingPrice && adapter.hasPrice() && adapter.cheese != null) {
            binding.fab.show()
        } else {
            binding.fab.hide()
        }
    }
}

class CheeseDetailViewModel
@ViewModelInject constructor(
    private val cheeseRepository: CheeseRepository,
    private val commentRepository: CommentRepository,
    private val priceRepository: PriceRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val forceLoad = MutableLiveData<Boolean>().apply {
        value = false
    }

    val cheese = AbsentLiveData.nullSwitchMap(forceLoad) {
        cheeseRepository.getCheese(getCheeseId(), it)
    }

    val price = AbsentLiveData.nullSwitchMap(forceLoad) {
        priceRepository.getPrice(getCheeseId())
    }

    val comments = AbsentLiveData.nullSwitchMap(forceLoad) {
        commentRepository.getComments(getCheeseId(), it)
    }

    val showBuyMessageEvent = SingleLiveEvent<Unit>()
    val showNewCommentEvent = SingleLiveEvent<Unit>()

    private fun getCheeseId(): Long = savedStateHandle["id"] ?: error("Cheese Id Missing")

    fun forceLoad() {
        forceLoad.postValue(true)
    }

    fun buy() {
        // Do some buying work
        showBuyMessageEvent.call()
    }

    fun onNewCommentClicked() {
        showNewCommentEvent.call()
    }

    fun newComment(comment: CharSequence) {
        commentRepository.addComment(getCheeseId(), comment.toString())
    }


}
