package com.dnnt.touch.ui.register

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import com.dnnt.touch.di.ActivityScoped
import com.dnnt.touch.ui.base.BaseViewModel
import com.dnnt.touch.util.SingleLiveEvent
import com.dnnt.touch.util.getString
import javax.inject.Inject
import com.dnnt.touch.R
import com.dnnt.touch.network.NetService
import com.dnnt.touch.util.toast
import io.reactivex.android.schedulers.AndroidSchedulers
import com.dnnt.touch.util.*

/**
 * Created by dnnt on 18-1-29.
 */
@ActivityScoped
class RegisterViewModel @Inject constructor(): BaseViewModel() {

    @Inject
    lateinit var netService: NetService

    val mNextStepEvent = SingleLiveEvent<Void>()
    val mGoRegisterEvent = SingleLiveEvent<Void>()
    val getCodeText = ObservableField<String>(getString(R.string.get_verification_code))

    fun getVerificationCode(phone: String){

    }

    fun register(){

    }

    fun codeVerification(verificationCode: String){
        netService.codeVerification(verificationCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ _: String? ->
                    mGoRegisterEvent.call()
                }, {
                    toast(R.string.wrong_verification_code)
                })
    }

}