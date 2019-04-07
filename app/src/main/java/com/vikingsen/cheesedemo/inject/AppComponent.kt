package com.vikingsen.cheesedemo.inject

import com.vikingsen.cheesedemo.App
import com.vikingsen.cheesedemo.model.work.SyncWorker
import com.vikingsen.cheesedemo.ux.cheesedetail.CheeseDetailFragment
import com.vikingsen.cheesedemo.ux.cheeselist.CheeseListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(target: App)
    fun inject(target: CheeseListFragment)
    fun inject(target: CheeseDetailFragment)

    // TODO: 4/7/19 REMOVE WITH WORK_INJECT
    fun inject(target: SyncWorker)
}
