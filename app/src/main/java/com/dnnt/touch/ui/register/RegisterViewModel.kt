package com.dnnt.touch.ui.register

import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import com.dnnt.touch.di.ActivityScoped
import com.dnnt.touch.ui.base.BaseViewModel
import com.dnnt.touch.base.SingleLiveEvent
import javax.inject.Inject
import com.dnnt.touch.R
import com.dnnt.touch.base.MyScheduler
import com.dnnt.touch.network.NetService
import com.dnnt.touch.util.toast
import io.reactivex.android.schedulers.AndroidSchedulers
import com.dnnt.touch.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by dnnt on 18-1-29.
 */
@ActivityScoped
class RegisterViewModel @Inject constructor(): BaseViewModel() {

    companion object {
        val TAG = "RegisterViewModel"
    }

    @Inject lateinit var mNetService: NetService
    @Inject lateinit var mScheduler: MyScheduler

    val mVerificationEvent = SingleLiveEvent<Void>()
    val mNextStepEvent = SingleLiveEvent<String>()
    val mFinishEvent = SingleLiveEvent<Void>()
    val mLoading = MutableLiveData<Boolean>()
    private var cookie = ""

    fun getVerificationCode(phone: String){
        if(phone.length != 11){
            toast("手机格式错误！")
            return
        }
        mNetService.getVerificationCode(phone)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    logi(TAG,it.headers().get("Set-Cookie"))
                    cookie = it.headers().get("Set-Cookie")?.split(";")?.get(0) ?: ""
                    toast(R.string.send_success)
                },{msg,_ ->
                    val failStr = getString(R.string.send_fail)
                    toast("$failStr,$msg")
                })
    }

    fun register(userName: String, password: String, password1: String,phone: String, code: String){
        when {
            userName.length < 4 || userName.length > 16 -> toast(R.string.user_name_hint)
            password.length < 6 -> toast(R.string.password_least_six)
            password != password1 -> toast(R.string.password_not_equal)
            else -> {
                val map = mapOf(Pair(USER_NAME,userName), Pair(PASSWORD,password),Pair(PHONE,phone),
                    Pair(VERIFICATION_CODE,code)
                )
                mLoading.value = true
                mNetService.register(map,cookie)
                        .delay(1000, TimeUnit.MILLISECONDS, mScheduler)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally{ mLoading.value = false }
                        .subscribe({
                            toast(R.string.register_success)
                            mFinishEvent.call()
                        },{msg,_ ->
                            toast(msg)
                        })
            }
        }

    }

    fun codeVerification(phone: String,verificationCode: String){
        if (verificationCode.length != 6){
            toast(R.string.wrong_verification_code)
            return
        }
        mNetService.codeVerification(phone,verificationCode,cookie)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mNextStepEvent.value = phone
                }, { _,_ ->
                    toast(R.string.wrong_verification_code)
                })
    }

    fun resetPassword(password: String, password1: String){
//        TODO
    }

}