package com.lenabru.networkapp.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.runBlocking


/*
 * Created by Lena Brusilovski on 2019-08-19
 */

class NetworkChangeCallback(val context: Context, private val networkStatusRepository: NetworkStatusRepository) : ConnectivityManager.NetworkCallback() {

    private val networkStatusManager: INetworkStatusManager = NetworkStatusManager()

    private val networkRequest: NetworkRequest =
        NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build()


    fun enable() {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(networkRequest, this)
    }

    fun disable() {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(this)
    }


    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        updateNetworkStatus()
    }

    override fun onUnavailable() {
        super.onUnavailable()
        updateNetworkStatus()
    }

    private fun updateNetworkStatus() {
        val status = networkStatusManager.getConnectivityStatus(context)
        runBlocking {
            networkStatusRepository.onNetworkStatusChanged(status)
        }
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        updateNetworkStatus()
    }
}