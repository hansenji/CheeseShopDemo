package com.vikingsen.cheesedemo.inject;


import android.app.Application;

public class Injector {
    private static AppComponent appComponent = null;

    public static void init(Application application) {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(application))
                .build();
    }

    public static AppComponent get() {
        if (appComponent == null) {
            throw new RuntimeException("Must call init before calling get.");
        }
        return appComponent;
    }
}
