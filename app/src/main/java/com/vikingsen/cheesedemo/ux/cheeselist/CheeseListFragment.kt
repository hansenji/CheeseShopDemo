package com.vikingsen.cheesedemo.ux.cheeselist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AnyThread
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vikingsen.cheesedemo.R
import com.vikingsen.cheesedemo.databinding.CheeseListFragmentBinding
import com.vikingsen.cheesedemo.inject.Injector
import com.vikingsen.cheesedemo.model.AbsentLiveData
import com.vikingsen.cheesedemo.model.Resource
import com.vikingsen.cheesedemo.model.SingleLiveEvent
import com.vikingsen.cheesedemo.model.database.cheese.Cheese
import com.vikingsen.cheesedemo.model.repository.cheese.CheeseRepository
import com.vikingsen.cheesedemo.ui.livedata.LiveDataObserverFragment
import com.vikingsen.cheesedemo.ui.recycler.SpaceItemDecoration
import com.vikingsen.inject.viewmodel.ViewModelInject
import com.vikingsen.inject.viewmodel.savedstate.SavedStateViewModelFactory
import javax.inject.Inject

class CheeseListFragment : LiveDataObserverFragment() {

    @Inject
    lateinit var savedStateViewModelFactoryFactory: SavedStateViewModelFactory.Factory

    private lateinit var binding: CheeseListFragmentBinding

    private val viewModel by viewModels<CheeseListViewModel> { savedStateViewModelFactoryFactory.create(this) }
    private val adapter by lazy { CheeseListAdapter(viewModel) }

    init {
        Injector.get().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.cheese_list_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark)
            setOnRefreshListener { viewModel.forceLoad() }
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(SpaceItemDecoration(binding.recyclerView.context, R.dimen.grid_space))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupViewModelObservers()
    }

    private fun setupViewModelObservers() {
        viewModel.cheeses.observeNotNull {
            when (it) {
                is Resource.Loading -> onLoading(it.data)
                is Resource.Success -> onSuccess(it.data)
                is Resource.Error -> onError(it.data)
            }
        }

        viewModel.showCheeseDetailEvent.observeNotNull {
            findNavController().navigate(CheeseListFragmentDirections.actionCheeseListFragmentToCheeseDetailFragment(it.id, it.name))
        }
    }

    private fun onLoading(cheeses: List<Cheese>?) {
        binding.swipeRefreshLayout.isRefreshing = true
        cheeses?.let {
            adapter.submitList(it)
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
}

class CheeseListViewModel
@ViewModelInject constructor(
    private val cheeseRepository: CheeseRepository
) : ViewModel() {

    private val forceLoad = MutableLiveData<Boolean>().apply {
        value = false
    }

    val cheeses: LiveData<Resource<List<Cheese>>> = AbsentLiveData.nullSwitchMap(forceLoad) {
        cheeseRepository.getCheeses(it)
    }
    val showCheeseDetailEvent = SingleLiveEvent<Cheese>()

    @MainThread
    fun onCheeseClicked(cheese: Cheese) {
        showCheeseDetailEvent.value = cheese
    }

    @AnyThread
    fun forceLoad() {
        forceLoad.postValue(true)
    }
}