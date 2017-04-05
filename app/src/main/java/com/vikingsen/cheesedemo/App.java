package com.vikingsen.cheesedemo;

import android.support.multidex.MultiDexApplication;

import com.evernote.android.job.JobManager;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.vikingsen.cheesedemo.inject.Injector;
import com.vikingsen.cheesedemo.job.AppJobCreator;
import com.vikingsen.cheesedemo.log.DebugTree;
import com.vikingsen.cheesedemo.log.ReleaseTree;

import javax.inject.Inject;

import timber.log.Timber;


public class App extends MultiDexApplication {

    @Inject
    AppJobCreator appJobCreator;

    public App() {
        super();
        Injector.init(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        setupLogging();

        Injector.get().inject(this);

        JobManager.create(this).addJobCreator(appJobCreator);
    }

    private void setupLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }
    }
}
