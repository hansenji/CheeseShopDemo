package com.vikingsen.cheesedemo.inject

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import com.vikingsen.cheesedemo.util.CoroutineContextProvider
import com.vikingsen.cheesedemo.util.CoroutineContextProvider.MainCoroutineContextProvider
import com.vikingsen.cheesedemo.ux.ViewModelModule
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Reusable
    fun provideSharedPreferences(application: Application): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)

    @Provides
    @Reusable
    fun provideConnectivityManager(application: Application) = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Reusable
    fun provideCoroutineContextProvider(): CoroutineContextProvider = MainCoroutineContextProvider

}
