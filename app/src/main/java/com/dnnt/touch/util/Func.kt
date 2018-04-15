package com.dnnt.touch.util

import android.content.Context
import android.text.TextUtils
import com.dnnt.touch.MyApplication
import com.dnnt.touch.R
import com.dnnt.touch.base.NetworkNotAvailableException
import com.dnnt.touch.been.Json
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import retrofit2.Response
import java.io.IOException

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

fun <T : Json<U>,U> Observable<Response<T>>.subscribe(onSuccess: (T) -> Unit, onFailure: (String,Int) -> Unit) =
    subscribe(onSuccess, onFailure, {}, {})

fun handleNetThrowable(throwable: Throwable){
    when (throwable) {
        is NetworkNotAvailableException -> toast(R.string.network_not_available)
        is IOException, is HttpException -> toast(R.string.network_error)
        is RuntimeException -> {
            val current = Thread.currentThread()
            current.uncaughtExceptionHandler.uncaughtException(current, throwable)
        }
        else -> toast(R.string.unknown_error)
    }
    throwable.printStackTrace()
}

fun <T> handleRequestFail(response: Response<T>){
    var msg = ""
    if (!TextUtils.isEmpty(response.message())) {
        msg = response.message()
    }
    msg = response.raw().code().toString() + " " + msg
    toast(msg)
}



