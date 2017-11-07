package com.vikingsen.cheesedemo.ux.cheeselist


import com.vikingsen.cheesedemo.inject.ActivityScope

import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(CheeseListModule::class))
interface CheeseListComponent {
    fun inject(target: CheeseListActivity)
}
