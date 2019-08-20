package com.lenabru.networkapp.network

import android.content.Context


/* 
 * Created by Lena Brusilovski on 2019-08-19
 */

object NetworkStatus{
    const val TYPE_WIFI = 1
    const val TYPE_MOBILE = 2
    const val TYPE_NOT_CONNECTED = 3
}

interface INetworkStatusManager {
    fun getConnectivityStatus(context: Context): Int
}