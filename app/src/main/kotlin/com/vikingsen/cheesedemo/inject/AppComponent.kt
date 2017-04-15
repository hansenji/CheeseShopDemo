package com.vikingsen.cheesedemo.inject

import com.vikingsen.cheesedemo.App
import com.vikingsen.cheesedemo.model.webservice.WebServiceModule
import com.vikingsen.cheesedemo.ux.cheesedetail.CheeseDetailComponent
import com.vikingsen.cheesedemo.ux.cheesedetail.CheeseDetailModule
import com.vikingsen.cheesedemo.ux.cheeselist.CheeseListComponent
import com.vikingsen.cheesedemo.ux.cheeselist.CheeseListModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, WebServiceModule::class))
interface AppComponent {
    fun include(module: CheeseListModule): CheeseListComponent
    fun include(cheeseDetailModule: CheeseDetailModule): CheeseDetailComponent

    fun inject(target: App)
}
