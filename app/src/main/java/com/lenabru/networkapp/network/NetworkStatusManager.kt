package com.lenabru.networkapp.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


/*
 * Created by Lena Brusilovski on 2019-08-19
 */

class NetworkStatusManager : INetworkStatusManager {

    override fun getConnectivityStatus(context: Context): Int {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = cm.activeNetwork

        network?.let {
            val networkCapabilities = cm.getNetworkCapabilities(network)
            val hasMobileNetwork =
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false

            if (hasMobileNetwork) {
                return NetworkStatus.TYPE_MOBILE
            }

            val hasWifi =
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
            if (hasWifi) {
                return NetworkStatus.TYPE_WIFI
            }

            if (!(hasMobileNetwork || hasWifi)) {
                return NetworkStatus.TYPE_NOT_CONNECTED
            }
        }

        return NetworkStatus.TYPE_NOT_CONNECTED
    }

}