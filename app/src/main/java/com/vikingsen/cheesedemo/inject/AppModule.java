package com.vikingsen.cheesedemo.inject;

import android.app.Application;

import com.vikingsen.cheesedemo.model.database.AppDatabaseConfig;

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
    DatabaseConfig provideDatabaseConfig(Application application) {
        return new AppDatabaseConfig(application);
    }

}
