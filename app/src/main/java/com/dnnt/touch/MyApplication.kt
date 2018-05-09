package com.dnnt.touch

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import com.dnnt.touch.been.User
import com.dnnt.touch.di.DaggerAppComponent
import com.dnnt.touch.receiver.NetworkReceiver
import com.dnnt.touch.base.CrashHandler
import com.raizlabs.android.dbflow.config.FlowManager
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import okhttp3.OkHttpClient
import javax.inject.Inject

/**
 * Created by dnnt on 17-12-23.
 */
class MyApplication: DaggerApplication() {

    companion object {
        lateinit var mOkHttpClient: OkHttpClient private set
        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context private set
        var mUser: User? = null
        lateinit var mToken: String
    }

    override fun onCreate() {
        super.onCreate()
        FlowManager.init(this)
    }

    @Inject fun setContext(context: Context){
        mContext = context
    }

    @Inject fun initCrashHandler(crashHandler: CrashHandler){}

    @Inject fun networkReceiverRegister(networkReceiver: NetworkReceiver){
        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(networkReceiver, intentFilter)
    }

    @Inject fun setOkHttpClient(okHttpClient: OkHttpClient){
        mOkHttpClient = okHttpClient
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
            DaggerAppComponent.builder()
                    .application(this)
                    .build()

}