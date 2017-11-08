package com.vikingsen.cheesedemo.ux

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.vikingsen.cheesedemo.inject.ViewModelKey
import com.vikingsen.cheesedemo.ux.cheeselist.CheeseListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CheeseListViewModel::class)
    internal abstract fun bindFeaturedViewModel(viewModel: CheeseListViewModel): ViewModel
}