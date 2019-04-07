package com.vikingsen.cheesedemo.model.webservice


import android.os.Build
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vikingsen.cheesedemo.BuildConfig
import com.vikingsen.cheesedemo.model.prefs.Prefs
import com.vikingsen.cheesedemo.model.webservice.converter.OffsetDateTimeAdapter
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.threeten.bp.OffsetDateTime
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Suppress("unused")
@Module
class WebServiceModule {

    @Provides
    @Singleton
    internal fun provideHttpLoggingInterceptor(prefs: Prefs): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(prefs.httpLoggingLevel)

    @Provides
    @Singleton
    internal fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addNetworkInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader("http.useragent", USER_AGENT)
            chain.proceed(requestBuilder.build())
        }
        return builder.build()
    }

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(OffsetDateTime::class.java, OffsetDateTimeAdapter())
            .create()
    }

    @Provides
    @Singleton
    internal fun provideCheeseService(okHttpClient: OkHttpClient, httpLoggingInterceptor: HttpLoggingInterceptor, gson: Gson): CheeseService {
        val client = okHttpClient.newBuilder().addNetworkInterceptor(httpLoggingInterceptor).build()
        val converterFactory = GsonConverterFactory.create(gson)

        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.SERVICE_BASE_URL)
                .client(client)
                .addConverterFactory(converterFactory)
                .build()

        return retrofit.create(CheeseService::class.java)
    }

    companion object {
        private val USER_AGENT = "${BuildConfig.USER_AGENT_APP_NAME} ${BuildConfig.VERSION_NAME} / " +
                "Android ${Build.VERSION.RELEASE} ${Build.VERSION.INCREMENTAL} / " +
                "${Build.MANUFACTURER} ${Build.MODEL}"
    }
}
