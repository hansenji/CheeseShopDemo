package com.vikingsen.cheesedemo.util;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NetworkUtil {

    public static final int TYPE_WIFI_DIRECT = 13; //WIFI_P2P

    private final ConnectivityManager connectivityManager;

    @Inject
    public NetworkUtil(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    public boolean isConnected() {
        return isConnected(true);
    }

    public boolean isConnected(boolean allowMobileNetwork) {
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if (info != null) {
            int type = info.getType();
            if (type == ConnectivityManager.TYPE_WIFI || type > ConnectivityManager.TYPE_WIMAX || allowMobileNetwork) { // NOPMD
                return info.isConnected() || type == TYPE_WIFI_DIRECT;
            }
        }
        return false;
    }
}
