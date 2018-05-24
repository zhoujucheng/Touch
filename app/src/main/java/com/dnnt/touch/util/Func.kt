package com.dnnt.touch.util

import android.content.Context
import android.text.TextUtils
import com.dnnt.touch.BuildConfig
import com.dnnt.touch.MyApplication
import com.dnnt.touch.R
import com.dnnt.touch.base.AlreadyInRequestException
import com.dnnt.touch.base.NetworkNotAvailableException
import com.dnnt.touch.been.Json
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.*

/**
 * Created by dnnt on 18-1-26.
 */
fun <T : Json<U>,U> Observable<Response<T>>.subscribe
        (onSuccess: (T) -> Unit, onFailure: (String,Int) -> Unit, @NonNull onComplete: () -> Unit,
         @NonNull onSubscribe: (Disposable) -> Unit ) =
        subscribe({
                    if (it.isSuccessful) {//请求成功
                        val json = it.body()
                        if (json != null){
                            if (json.successful) {//服务器操作成功
                                onSuccess(json)
                            } else {
                                onFailure(json.msg,json.code)
                            }
                        }else{
                            toast(R.string.server_error)
                        }
                    } else {
                        handleRequestFail(it)
                    }
                }, { handleNetThrowable(it) }, onComplete, onSubscribe)!!

//onFailure并不是请求失败的，是请求成功的，但由于某些原因操作失败而调用，如因密码错误而登录失败
fun <T : Json<U>,U> Observable<Response<T>>.subscribe(onSuccess: (T) -> Unit, onFailure: (String,Int) -> Unit) =
    subscribe(onSuccess, onFailure, {}, {})

fun handleNetThrowable(throwable: Throwable){
    logi("Func.kt","handleNetThrowable,${throwable.javaClass.name}")
    when (throwable) {
        is NetworkNotAvailableException -> toast(R.string.network_not_available)
        is IOException, is HttpException -> {
            toast(R.string.network_error)
            throwable.printStackTrace()
        }
        is RuntimeException -> {
            val current = Thread.currentThread()
            current.uncaughtExceptionHandler.uncaughtException(current, throwable)
        }
        !is AlreadyInRequestException -> {
            toast(R.string.unknown_error)
            throwable.printStackTrace()
        }
    }
}

fun <T> saveRequestFail(response: Response<T>){
    val code = response.raw().code()
    val path = MyApplication.mContext.externalCacheDir.path + "/request_err"
    File(path).mkdir()
    val time = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS").format(Date(System.currentTimeMillis()))
    val file = File("$path/req_${code}_$time.html")
    val ins = response.errorBody()?.byteStream()
    val outs = file.outputStream()
    launch(CommonPool) {
        try {
            ins?.copyTo(outs)
        }finally {
            ins?.close()
            outs.close()
        }
    }
}

fun <T> handleRequestFail(response: Response<T>){
    var msg = ""
    if (!TextUtils.isEmpty(response.message())) {
        msg = response.message()
    }
    if (BuildConfig.DEBUG){
        saveRequestFail(response)
    }
    toast("${response.code()} $msg")
}


fun getSSL():Pair<SSLContext,X509TrustManager>{
    val keyStoreType = KeyStore.getDefaultType()
    val keyStore = KeyStore.getInstance(keyStoreType)
    keyStore.load(null, null)
//    val caIS = MyApplication.mContext.assets.open("tomcat_local.cer")
    val caIS = MyApplication.mContext.assets.open("tomcat_server.cer")
    val certificateFactory = CertificateFactory.getInstance("X.509")
    val ca = certificateFactory.generateCertificate(caIS)
    caIS.close()
    keyStore.setCertificateEntry("tomcat", ca)
    val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
    val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
    tmf.init(keyStore)
    val tms = tmf.trustManagers
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, tms, null)
    return Pair(sslContext,tms[0] as X509TrustManager)
}

fun removeSensitiveInfo(context: Context){
    val editor = context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE).edit()
    editor.remove(PASSWORD)
    editor.remove(TOKEN)
    editor.remove(ID)
    editor.apply()
    launch(UI) {
        MyApplication.mUser = null
        MyApplication.mToken = ""
    }
}

fun finishAllActivity(){
    val list = MyApplication.activityList
    for (i in list.size-1 downTo 0){
        list[i].finish()
    }
}