package com.vikingsen.cheesedemo.ux.cheesedetail

import com.vikingsen.cheesedemo.inject.ActivityScope

import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(CheeseDetailModule::class))
interface CheeseDetailComponent {
    fun inject(target: CheeseDetailActivity)
}
