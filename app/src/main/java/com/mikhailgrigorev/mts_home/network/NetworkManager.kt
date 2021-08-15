package com.mikhailgrigorev.mts_home.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

interface NetworkManager {

    fun register(listener: OnNetworkStateChangeListener)

    fun unregister()

    interface OnNetworkStateChangeListener {
        fun onNetworkStateChanged(isConnected: Boolean)
    }
}


class NetworkManagerImpl(private val context: Context) : NetworkManager {

    private var listener: NetworkManager.OnNetworkStateChangeListener? = null
    private var networkReceiver: NetworkReceiver? = null
    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    override fun register(listener: NetworkManager.OnNetworkStateChangeListener) {
        this.listener = listener
        initNetworkCallback()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initNetworkCallback() {
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                listener?.onNetworkStateChanged(isConnected = true)
            }

            override fun onLost(network: Network) {
                listener?.onNetworkStateChanged(isConnected = false)
            }
        }

        networkCallback?.let { getConnectivityManager().registerDefaultNetworkCallback(it) }
    }

    private fun getConnectivityManager(): ConnectivityManager {
        return context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private fun initNetworkReceiver() {
        networkReceiver = NetworkReceiver()
        context.registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun unregister() {
        listener = null

        networkCallback?.let { getConnectivityManager().unregisterNetworkCallback(it) }
    }

    inner class NetworkReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            listener?.onNetworkStateChanged(context.checkForNetwork())
        }
    }
}


fun Context?.checkForNetwork(): Boolean {
    if (this == null) return false

    val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

    return when {
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}