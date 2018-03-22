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

    @Inject lateinit var mNetService: NetService
    @Inject lateinit var mScheduler: MyScheduler

    val mVerificationEvent = SingleLiveEvent<Void>()
    val mNextStepEvent = SingleLiveEvent<Void>()
    val mFinishEvent = SingleLiveEvent<Void>()
    val mLoading = MutableLiveData<Boolean>()

    fun getVerificationCode(phone: String){

        if (TextUtils.isEmpty(phone)){
            toast(R.string.phone_empty)
            return
        }

        mNetService.getVerificationCode(phone)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({_: String? ->
                    toast(R.string.send_success)
                },{
                    toast(R.string.send_fail)
                })
    }

    fun register(userName: String, password: String, password1: String){
        when {
            TextUtils.isEmpty(userName) -> toast(R.string.user_name_empty)
            password.length < 6 -> toast(R.string.password_least_six)
            password != password1 -> toast(R.string.password_not_equal)
            else -> {
                val map = mapOf(Pair(USER_NAME,userName), Pair(PASSWORD,password))
                mLoading.value = true
                mNetService.register(map)
                        .delay(1000, TimeUnit.MILLISECONDS, mScheduler)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally{ mLoading.value = false }
                        .subscribe({
                            toast(R.string.register_success)
                            mFinishEvent.call()
                        },{
                            toast(R.string.register_fail)
                        })
            }
        }

    }

    fun codeVerification(verificationCode: String){
        if (verificationCode.length != 6){
            toast(R.string.wrong_verification_code)
            return
        }
        mNetService.codeVerification(verificationCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ _: String? ->
                    mNextStepEvent.call()
                }, {
                    toast(R.string.wrong_verification_code)
                })
    }

    fun resetPassword(password: String, password1: String){
//        TODO
    }

}