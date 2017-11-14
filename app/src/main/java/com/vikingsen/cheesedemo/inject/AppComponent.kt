package com.vikingsen.cheesedemo.inject

import com.vikingsen.cheesedemo.App
import com.vikingsen.cheesedemo.model.webservice.WebServiceModule
import com.vikingsen.cheesedemo.ux.cheesedetail.CheeseDetailActivity
import com.vikingsen.cheesedemo.ux.cheeselist.CheeseListActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, WebServiceModule::class))
interface AppComponent {
    fun inject(target: App)
    fun inject(target: CheeseListActivity)
    fun inject(target: CheeseDetailActivity)
}
