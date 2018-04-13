package com.dnnt.touch.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.dnnt.touch.MyApplication
import com.dnnt.touch.netty.NettyService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by dnnt on 18-1-27.
 * 网络变化监听器
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
            if (MyApplication.mUser != null){
                context.startService(Intent(context,NettyService::class.java))
            }
            if (networkInfo.type == ConnectivityManager.TYPE_WIFI){
                WIFI
            }else{
                MOBILE_DATA
            }
        }else{
            if (MyApplication.mUser != null){
                context.stopService(Intent(context,NettyService::class.java))
            }
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