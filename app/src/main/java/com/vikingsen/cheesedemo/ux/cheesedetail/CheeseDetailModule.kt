package com.vikingsen.cheesedemo.ux.cheesedetail


import dagger.Module
import dagger.Provides

@Module
class CheeseDetailModule(private val view: CheeseDetailContract.View) {

    @Provides
    fun provideView(): CheeseDetailContract.View {
        return view
    }
}
