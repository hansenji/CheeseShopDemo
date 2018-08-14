package com.vikingsen.cheesedemo.inject


import android.app.Application

object Injector {
    private lateinit var appComponent: AppComponent

    @JvmStatic
    @Synchronized
    fun init(application: Application) {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(application))
                .build()
    }

    @JvmStatic
    @Synchronized
    fun get(): AppComponent = appComponent
}
