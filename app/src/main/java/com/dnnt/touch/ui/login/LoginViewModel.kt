package com.dnnt.touch.ui.login

import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import android.util.Log
import com.dnnt.touch.ui.base.BaseViewModel
import com.dnnt.touch.MyApplication
import com.dnnt.touch.R
import com.dnnt.touch.been.User
import com.dnnt.touch.di.ActivityScoped
import com.dnnt.touch.network.NetService
import com.dnnt.touch.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by dnnt on 18-1-26.
 */
@ActivityScoped
class LoginViewModel @Inject constructor(netService: NetService): BaseViewModel() {

    private val mNetService = netService
    val mLoginEvent = SingleLiveEvent<Void>()
    val mLoading = MutableLiveData<Boolean>()

    fun login(userName: String, pwd: String){
        when {
            TextUtils.isEmpty(userName) -> toast(R.string.user_not_empty)
            pwd.length < 6 -> toast(R.string.wrong_pwd)
            else -> {
                val map = mapOf(Pair(USER_NAME,userName), Pair(PASSWORD,pwd))
                mLoading.value = true
                mNetService.login(map)
                        .delay(1000,TimeUnit.MILLISECONDS,MyApplication.mScheduler)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally{ mLoading.value = false }
                        .subscribe({ u: User? ->
                            MyApplication.mUser = u
                            mLoginEvent.call()
                        }, {
                            toast(R.string.login_fail)
                        })

            }
        }
    }

}





