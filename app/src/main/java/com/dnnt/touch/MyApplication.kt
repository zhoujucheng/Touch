package com.dnnt.touch

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import com.dnnt.touch.been.User
import com.dnnt.touch.di.DaggerAppComponent
import com.dnnt.touch.receiver.NetworkReceiver
import com.dnnt.touch.util.CrashHandler
import com.dnnt.touch.util.MyScheduler
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.Scheduler
import java.util.concurrent.ExecutorService
import javax.inject.Inject

/**
 * Created by dnnt on 17-12-23.
 */
class MyApplication: DaggerApplication() {

    companion object {
        lateinit var mExecutorService: ExecutorService private set
        lateinit var mScheduler: Scheduler private set
        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context private set
        var mUser: User? = null
    }

    @Inject
    fun setScheduler(scheduler: MyScheduler){
        mScheduler = scheduler
    }

    @Inject
    fun setContext(context: Context){
        mContext = context
    }

    @Inject
    fun setExecutorService(executorService: ExecutorService){
        mExecutorService = executorService
    }

    @Inject
    fun initCrashHandler(crashHandler: CrashHandler){}

    @Inject
    fun networkReceiverRigister(networkReceiver: NetworkReceiver){
        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(networkReceiver, intentFilter)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
            DaggerAppComponent.builder()
                    .application(this)
                    .build()

}