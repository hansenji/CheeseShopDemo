package com.vikingsen.cheesedemo.inject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import com.vikingsen.cheesedemo.model.database.AppDatabaseConfig;
import com.vikingsen.cheesedemo.util.SchedulerProvider;
import com.vikingsen.cheesedemo.util.SchedulerProvider.AppSchedulerProvider;

import org.dbtools.android.domain.config.DatabaseConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class AppModule {
    private final Application application;

    AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    ConnectivityManager provideConnectivityManager(Application application) {
        return (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    @Singleton
    DatabaseConfig provideDatabaseConfig(Application application) {
        return new AppDatabaseConfig(application);
    }

    @Provides
    @Singleton
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

}
