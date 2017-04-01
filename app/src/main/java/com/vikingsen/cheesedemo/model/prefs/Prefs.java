package com.vikingsen.cheesedemo.model.prefs;


import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.logging.HttpLoggingInterceptor;

@Singleton
public class Prefs {

    private static final String PREF_HTTP_LOG_LEVEL = "dev_http_log_level";

    private final SharedPreferences preferences;

    @Inject
    Prefs(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public HttpLoggingInterceptor.Level getHttpLoggingLevel() {
        String logLevelAsString = preferences.getString(PREF_HTTP_LOG_LEVEL, HttpLoggingInterceptor.Level.BASIC.name());
        return HttpLoggingInterceptor.Level.valueOf(logLevelAsString);
    }
}
