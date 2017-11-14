package com.vikingsen.cheesedemo.model.webservice


import android.os.Build
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.vikingsen.cheesedemo.BuildConfig
import com.vikingsen.cheesedemo.model.prefs.Prefs
import com.vikingsen.cheesedemo.model.webservice.converter.LocalDateTimeDeserializer
import com.vikingsen.cheesedemo.model.webservice.converter.LocalDateTimeSerializer
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.threeten.bp.LocalDateTime
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Suppress("unused")
@Module
class WebServiceModule {

    @Provides
    @Singleton
    internal fun provideHttpLoggingInterceptor(prefs: Prefs): HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(prefs.httpLoggingLevel)

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
    internal fun provideObjectMapper(): ObjectMapper {
        val module = SimpleModule()
        module.addSerializer(LocalDateTimeSerializer())
        module.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer())

        val objectMapper = jacksonObjectMapper()
        objectMapper.registerModule(module)

        return objectMapper
    }

    @Provides
    @Singleton
    internal fun provideCheeseSerivce(okHttpClient: OkHttpClient, httpLoggingInterceptor: HttpLoggingInterceptor, objectMapper: ObjectMapper): CheeseService {
        val client = okHttpClient.newBuilder().addNetworkInterceptor(httpLoggingInterceptor).build()
        val converterFactory = JacksonConverterFactory.create(objectMapper)

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
