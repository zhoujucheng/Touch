package com.dnnt.touch.util

import android.text.TextUtils
import com.dnnt.touch.R
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
        (onSuccess: (U?) -> Unit, onFailure: (String) -> Unit, @NonNull onComplete: () -> Unit,
         @NonNull onSubscribe: (Disposable) -> Unit ) =
        subscribe(
                {
                    if (it.isSuccessful) {
                        val t = it.body()
                        if (t != null){
                            if (t.successful) {
                                onSuccess(t.obj)
                            } else {
                                onFailure(t.msg)
                            }
                        }else{
                            toast(R.string.server_error)
                        }
                    } else {
                        var msg = ""
                        if (!TextUtils.isEmpty(it.message())) {
                            msg = it.message()
                        }
                        msg = it.raw().code().toString() + " " + msg
                        toast(msg)
                    }
                },
                {
                    when (it) {
                        is NetworkNotAvailableException -> toast(R.string.network_not_available)
                        is IOException, is HttpException -> toast(R.string.network_error)
                        is RuntimeException -> {
                            val current = Thread.currentThread()
                            current.uncaughtExceptionHandler.uncaughtException(current, it)
                        }
                        else -> toast(R.string.unknown_error)
                    }
                    it.printStackTrace()
                }, onComplete, onSubscribe)!!

fun <T : Json<U>,U> Observable<Response<T>>.subscribe(onFailure: (String) -> Unit, onSuccess: (U?) -> Unit) =
    subscribe(onSuccess, onFailure, {}, {})

