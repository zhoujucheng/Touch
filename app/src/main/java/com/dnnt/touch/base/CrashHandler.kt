package com.dnnt.touch.base

import android.content.Context
import android.os.Build
import android.os.Process
import com.dnnt.touch.MyApplication
import com.dnnt.touch.network.NetService
import com.dnnt.touch.receiver.NetworkReceiver
import com.dnnt.touch.util.CRASH_DIR
import com.dnnt.touch.util.loge
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*
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
    @Inject lateinit var mNetService: NetService

    init {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }


    override fun uncaughtException(t: Thread?, e: Throwable?) {
        loge("CrashHandler","uncaughtException")
        try {
            if (e != null){
                e.printStackTrace()
                val path = "${mContext.externalCacheDir.path}/$CRASH_DIR"
                File(path).mkdirs()
                val time = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS").format(Date(System.currentTimeMillis()))
                val userId = MyApplication.mUser?.id ?: 0L
                val file = File("$path/err_${time}_$userId.txt")
                val pw = PrintWriter(BufferedWriter(FileWriter(file)))
                pw.use {
                    writeBaseMsg(it)
                    e.printStackTrace(it)
                }
                upLoadException(file)
            }
        }finally {
            mDefaultHandler?.uncaughtException(t,e) ?: Process.killProcess(Process.myPid())
        }

    }

    private fun writeBaseMsg(pw: PrintWriter){
        pw.println("OS Version: ${Build.VERSION.RELEASE}_${Build.VERSION.SDK_INT}")
        pw.println("Vendor: ${Build.MANUFACTURER}")
        pw.println("Model: ${Build.MODEL}")
    }

    @Throws(Throwable::class)
    private fun upLoadException(errFile: File){
        val fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), errFile)
        val filePart = MultipartBody.Part.createFormData("uploadFile", errFile.name, fileBody)
        runBlocking {
            val l = launch(CommonPool) {
                //I don't care if it is successful or not
                if (NetworkReceiver.isNetUsable()){
                    val response = mNetService.uploadErrFile(filePart).execute()
                    if(response.isSuccessful && response.body()?.successful == true){
                        errFile.delete()
                    }
                }
            }
            l.join()
        }
    }
}
