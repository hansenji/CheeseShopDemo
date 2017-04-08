package com.vikingsen.cheesedemo.model.webservice;


import android.os.Build;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.vikingsen.cheesedemo.BuildConfig;
import com.vikingsen.cheesedemo.model.prefs.Prefs;
import com.vikingsen.cheesedemo.model.webservice.converter.LocalDateTimeDeserializer;
import com.vikingsen.cheesedemo.model.webservice.converter.LocalDateTimeSerializer;

import org.threeten.bp.LocalDateTime;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public class WebServiceModule {

    private static final String USER_AGENT = BuildConfig.USER_AGENT_APP_NAME + " " +
                                             BuildConfig.VERSION_NAME + " / Android " +
                                             Build.VERSION.RELEASE + " " +
                                             Build.VERSION.INCREMENTAL + " / " +
                                             Build.MANUFACTURER + " " +
                                             Build.MODEL;

    @Provides
    @Singleton
    HttpLoggingInterceptor provideHttpLoggingInterceptor(Prefs prefs) {
        return new HttpLoggingInterceptor().setLevel(prefs.getHttpLoggingLevel());
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addNetworkInterceptor(chain -> {
            Request.Builder requestBuilder = chain.request().newBuilder();
            requestBuilder.addHeader("http.useragent", USER_AGENT);
            return chain.proceed(requestBuilder.build());
        });
        return builder.build();
    }

    @Provides
    @Singleton
    ObjectMapper provideObjectMapper() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(new LocalDateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);

        return objectMapper;
    }

    @Provides
    @Singleton
    CheeseService provideCheeseSerivce(OkHttpClient okHttpClient, HttpLoggingInterceptor httpLoggingInterceptor, ObjectMapper objectMapper) {
        OkHttpClient client = okHttpClient.newBuilder().addNetworkInterceptor(httpLoggingInterceptor).build();
        JacksonConverterFactory converterFactory = JacksonConverterFactory.create(objectMapper);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVICE_BASE_URL)
                .client(client)
                .addConverterFactory(converterFactory)
                .build();

        return retrofit.create(CheeseService.class);
    }
}
