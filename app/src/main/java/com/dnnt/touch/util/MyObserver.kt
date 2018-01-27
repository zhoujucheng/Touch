package com.dnnt.touch.util

import android.text.TextUtils
import android.util.Log
import com.dnnt.touch.been.Json
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.Response
import java.io.IOException

/**
 * Created by dnnt on 18-1-25.
 */
abstract class MyObserver<T : Json<U>,U> : Observer<Response<T>> {

    private val TAG = "MyObserver"

    override fun onSubscribe(d: Disposable) {}

    override fun onNext(tResponse: Response<T>) {
        if (tResponse.isSuccessful) {
            val t = tResponse.body()
            if (t!!.successful) {
                onSuccess(t)
            } else {
                onFailure(t.msg)
            }
        } else {
            var msg = ""
            if (!TextUtils.isEmpty(tResponse.message())) {
                msg = tResponse.message()
            }
            msg = tResponse.raw().code().toString() + " " + msg
            onFailure(msg)
        }
    }

    override fun onError(e: Throwable) {
        var msg = ""
        if (e is IOException || e is HttpException) {
            msg = "网络出错"
        } else if (e is NullPointerException || e is IllegalArgumentException || e is IllegalStateException) {
            val current = Thread.currentThread()
            current.uncaughtExceptionHandler.uncaughtException(current, e)
        } else {
            msg = "发生未知错误"
        }
        onFailure(msg)
        msg = "onError, " + e.toString()
        Log.i(TAG, msg)
        e.printStackTrace()
    }

    override fun onComplete() {}

    abstract fun onSuccess(t: T)

    abstract fun onFailure(msg: String)
}


