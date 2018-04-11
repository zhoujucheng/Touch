package com.dnnt.touch.ui.login

import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import com.dnnt.touch.ui.base.BaseViewModel
import com.dnnt.touch.MyApplication
import com.dnnt.touch.R
import com.dnnt.touch.base.MyScheduler
import com.dnnt.touch.base.SingleLiveEvent
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
class LoginViewModel @Inject constructor(): BaseViewModel() {

    @Inject lateinit var mScheduler: MyScheduler
    @Inject lateinit var mNetService: NetService
    val mLoginEvent = SingleLiveEvent<Void>()
    val mLoading = MutableLiveData<Boolean>()


    fun login(nameOrPhone: String, password: String){
        when {
            nameOrPhone.length < NAME_MIN_LENGTH || nameOrPhone.length > NAME_MAX_LENGTH -> toast(R.string.name_or_phone_wrong)
            password.length < 6 -> toast(R.string.wrong_password)
            else -> {
                val map = mapOf(Pair(NAME_OR_PHONE,nameOrPhone), Pair(PASSWORD,password))
                mLoading.value = true
                mNetService.login(map)
                        .delay(1000,TimeUnit.MILLISECONDS,mScheduler)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally{ mLoading.value = false }
                        .subscribe({
                            MyApplication.mUser = it.body()?.obj
                            MyApplication.mUser?.nickname = MyApplication.mUser?.userName
                            logi("Application User Login",MyApplication.mUser?.userName ?: "null")
                            mLoginEvent.call()
                        }, {_,_ ->
                            toast(R.string.login_fail)
                        })
            }
        }
    }

}





