package com.dnnt.touch.ui.login

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.ObservableBoolean
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.dnnt.touch.BaseVM
import com.dnnt.touch.MyApplication
import com.dnnt.touch.R
import com.dnnt.touch.been.Json
import com.dnnt.touch.been.User
import com.dnnt.touch.di.ActivityScoped
import com.dnnt.touch.network.NetService
import io.reactivex.android.schedulers.AndroidSchedulers
import com.dnnt.touch.subscribe
import com.dnnt.touch.util.PASSWORD
import com.dnnt.touch.util.USER_NAME
import com.dnnt.touch.util.toast
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by dnnt on 18-1-26.
 */
@ActivityScoped
class LoginVM @Inject constructor(netService: NetService): BaseVM() {

    private val mNetService = netService
    val showProgress: ObservableBoolean = ObservableBoolean(false)

    fun login(userName: String, pwd: String){
        when {
            TextUtils.isEmpty(userName) -> toast(R.string.user_not_empty)
            pwd.length < 6 -> toast(R.string.wrong_pwd)
            else -> {
                val map = mapOf(Pair(USER_NAME,userName), Pair(PASSWORD,pwd))
                showProgress.set(true)
                mNetService.login(map)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally{ showProgress.set(false) }
                        .subscribe({ toast(R.string.login_fail) }){ u: User? ->
                            MyApplication.mUser = u
                        }

            }
        }
    }
}





