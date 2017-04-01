package com.vikingsen.cheesedemo;

import android.support.multidex.MultiDexApplication;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.vikingsen.cheesedemo.inject.Injector;
import com.vikingsen.cheesedemo.log.DebugTree;
import com.vikingsen.cheesedemo.log.ReleaseTree;

import timber.log.Timber;


public class App extends MultiDexApplication {

    public App() {
        super();
        Injector.init(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);

        setupLogging();
    }

    private void setupLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }
    }
}
