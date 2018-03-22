package com.dnnt.touch.base

import android.content.Context
import android.os.Process
import com.dnnt.touch.MyApplication
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by dnnt on 17-12-23.
 */
@Singleton
class CrashHandler @Inject constructor(context: Context): Thread.UncaughtExceptionHandler{

    private val mDefaultHandler: Thread.UncaughtExceptionHandler? = Thread.getDefaultUncaughtExceptionHandler()
    private val mContext = context



    init {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }


    override fun uncaughtException(t: Thread?, e: Throwable?) {
        if (e != null){
            e.printStackTrace()
//            TODO("handle crash")
        }
        mDefaultHandler?.uncaughtException(t,e) ?: Process.killProcess(Process.myPid())
    }

    fun upLoadException(){
//        TODO("up load exception to server")
    }

}
