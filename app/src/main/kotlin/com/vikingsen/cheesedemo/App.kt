package com.vikingsen.cheesedemo

import android.support.multidex.MultiDexApplication
import com.evernote.android.job.JobManager
import com.jakewharton.threetenabp.AndroidThreeTen
import com.vikingsen.cheesedemo.inject.Injector
import com.vikingsen.cheesedemo.job.AppJobCreator
import com.vikingsen.cheesedemo.log.DebugTree
import com.vikingsen.cheesedemo.log.ReleaseTree
import timber.log.Timber
import javax.inject.Inject


class App : MultiDexApplication() {

    @Inject
    lateinit var appJobCreator: AppJobCreator

    init {
        Injector.init(this)
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        setupLogging()

        Injector.get().inject(this)

        JobManager.create(this).addJobCreator(appJobCreator)
    }

    private fun setupLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
    }
}
