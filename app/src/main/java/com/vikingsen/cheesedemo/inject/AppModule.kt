package com.vikingsen.cheesedemo.inject

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import com.vikingsen.cheesedemo.model.database.DatabaseModule
import com.vikingsen.cheesedemo.util.SchedulerProvider
import com.vikingsen.cheesedemo.util.SchedulerProvider.AppSchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = arrayOf(DatabaseModule::class))
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Provides
    @Singleton
    fun provideConnectivityManager(application: Application): ConnectivityManager {
        return application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider {
        return AppSchedulerProvider()
    }

}
