package com.dnnt.touch.base

import android.content.Context
import android.content.Intent
import android.os.Process
import com.dnnt.touch.MyApplication
import com.dnnt.touch.netty.NettyService
import com.dnnt.touch.util.loge
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by dnnt on 17-12-23.
 * 程序异常处理
 */
@Singleton
class CrashHandler @Inject constructor(context: Context): Thread.UncaughtExceptionHandler{

    private val mDefaultHandler: Thread.UncaughtExceptionHandler? = Thread.getDefaultUncaughtExceptionHandler()
    private val mContext = context

    init {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }


    override fun uncaughtException(t: Thread?, e: Throwable?) {
//        mContext.stopService(Intent(mContext,NettyService::class.java))
        loge("CrashHandler","uncaughtException")
        if (e != null){
            e.printStackTrace()
//            TODO("handle crash")
        }
        loge("",e?.message)
        mDefaultHandler?.uncaughtException(t,e) ?: Process.killProcess(Process.myPid())
    }

    fun upLoadException(){
//        TODO("up load exception to server")
    }

}
