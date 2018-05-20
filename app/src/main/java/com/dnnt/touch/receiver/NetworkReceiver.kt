package com.dnnt.touch.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.dnnt.touch.MyApplication
import com.dnnt.touch.netty.NettyService
import com.dnnt.touch.util.MOBILE_DATA
import com.dnnt.touch.util.NO_NETWORK
import com.dnnt.touch.util.WIFI
import com.dnnt.touch.util.logi
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by dnnt on 18-1-27.
 * 网络变化监听器
 */
@Singleton
class NetworkReceiver @Inject constructor() : BroadcastReceiver() {

    companion object {

        var netStatus = NO_NETWORK
            private set

        fun isNetUsable() = netStatus > NO_NETWORK
    }

    private val listeners: MutableList<NetworkChangeListener> = mutableListOf()

    override fun onReceive(context: Context, intent: Intent) {
        netStatus = getNetStatus(context)
        listeners.forEach { it.networkChanged(netStatus) }
    }

    @Inject fun initStatus(context: Context){
        netStatus = getNetStatus(context)
    }

    private fun getNetStatus(context: Context): Int{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return if (networkInfo != null && networkInfo.isConnected){
            if (networkInfo.type == ConnectivityManager.TYPE_WIFI){
                WIFI
            }else{
                MOBILE_DATA
            }

        }else{
            NO_NETWORK
        }

    }

    fun addListener(listener: NetworkChangeListener) =
        listeners.add(listener)

    fun removeListener(listener: NetworkChangeListener){
        listeners.remove(listener)
        logi("remove listener","remove")
    }

    interface NetworkChangeListener{
        fun networkChanged(status: Int)
    }
}