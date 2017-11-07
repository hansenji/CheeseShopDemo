package com.vikingsen.cheesedemo.util

import android.net.ConnectivityManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkUtil
@Inject constructor(private val connectivityManager: ConnectivityManager) {

    val isConnected: Boolean
        get() = isConnected(true)

    fun isConnected(allowMobileNetwork: Boolean): Boolean {
        val info = connectivityManager.activeNetworkInfo

        if (info != null) {
            val type = info.type
            if (type == ConnectivityManager.TYPE_WIFI || type > ConnectivityManager.TYPE_WIMAX || allowMobileNetwork) {
                return info.isConnected || type == TYPE_WIFI_DIRECT
            }
        }
        return false
    }

    companion object {
        val TYPE_WIFI_DIRECT = 13 //WIFI_P2P
    }
}
