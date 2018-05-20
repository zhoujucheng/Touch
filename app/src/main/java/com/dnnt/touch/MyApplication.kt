package com.dnnt.touch

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import com.dnnt.touch.been.User
import com.dnnt.touch.di.DaggerAppComponent
import com.dnnt.touch.receiver.NetworkReceiver
import com.dnnt.touch.base.CrashHandler
import com.dnnt.touch.ui.login.LoginActivity
import com.dnnt.touch.util.logi
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
        //此处mUser.friendId 代表与之对话的用户ID
        var mUser: User? = null
        lateinit var mToken: String
        private var count = 0
        val activityList = mutableListOf<Activity>()

        fun isOnBackGround() = count == 0
    }

    override fun onCreate() {
        super.onCreate()
        FlowManager.init(this)
        val callbacks = LifecycleCallBacks()
        this.registerActivityLifecycleCallbacks(callbacks)
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

    private inner class LifecycleCallBacks : ActivityLifecycleCallbacks{
        override fun onActivityPaused(activity: Activity?) {

        }

        override fun onActivityResumed(activity: Activity?) {
        }

        override fun onActivityStarted(activity: Activity?) {
            count++
            logi("LifecycleCallBackStarted","count,$count")
        }

        override fun onActivityDestroyed(activity: Activity?) {
            activity?.let {
                activityList.remove(it)
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        }

        override fun onActivityStopped(activity: Activity?) {
            count--
            logi("LifecycleCallBackStopped","count,$count")
        }

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            activity?.let {
                activityList.add(activity)
            }
        }

    }

}