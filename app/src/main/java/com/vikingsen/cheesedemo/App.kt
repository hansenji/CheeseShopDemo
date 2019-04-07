package com.vikingsen.cheesedemo

import androidx.multidex.MultiDexApplication
import com.jakewharton.threetenabp.AndroidThreeTen
import com.vikingsen.cheesedemo.inject.Injector
import com.vikingsen.cheesedemo.log.DebugTree
import com.vikingsen.cheesedemo.log.ReleaseTree
import timber.log.Timber


class App : MultiDexApplication() {

    init {
        Injector.init(this)
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        setupLogging()

        Injector.get().inject(this)

//        JobManager.create(this).addJobCreator(appJobCreator)
    }

    private fun setupLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
    }
}
