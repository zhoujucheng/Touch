package com.dnnt.touch.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by dnnt on 18-1-27.
 */
@Singleton
class NetworkReceiver @Inject constructor() : BroadcastReceiver() {

    companion object {
        const val NO_NETWORK = 0

        const val MOBILE_DATA = 1
        const val WIFI = 2

        var netStatus = NO_NETWORK
            private set

        fun isNetUsable() = netStatus > NO_NETWORK
    }

    private val listeners: MutableList<NetworkChangeListener> = mutableListOf()

    override fun onReceive(context: Context, intent: Intent) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        netStatus = if (networkInfo != null && networkInfo.isConnected){
            if (networkInfo.type == ConnectivityManager.TYPE_WIFI){
                WIFI
            }else{
                MOBILE_DATA
            }
        }else{
            NO_NETWORK
        }

        listeners.forEach { it.networkChanged(netStatus) }
    }

    fun addListener(listener: NetworkChangeListener) =
        listeners.add(listener)

    fun removeListener(listener: NetworkChangeListener) =
        listeners.remove(listener)

    interface NetworkChangeListener{
        fun networkChanged(status: Int)
    }
}