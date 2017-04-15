package com.vikingsen.cheesedemo.ux.cheeselist

import dagger.Module
import dagger.Provides

@Module
class CheeseListModule(private val view: CheeseListContract.View) {

    @Provides
    internal fun provideView(): CheeseListContract.View {
        return view
    }
}
