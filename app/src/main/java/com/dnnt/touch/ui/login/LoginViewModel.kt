package com.dnnt.touch.ui.login

import android.arch.lifecycle.MutableLiveData
import android.content.Context
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
            password.length < PWD_MIN_LEN || password.length > PWD_MAX_LEN -> toast(R.string.wrong_password)
            else -> {
                val map = hashMapOf(Pair(NAME_OR_PHONE,nameOrPhone), Pair(PASSWORD,password))
                mLoading.value = true
                mNetService.login(map)
                        .delay(1000,TimeUnit.MILLISECONDS,mScheduler)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally{ mLoading.value = false }
                        .subscribe({
                            MyApplication.mUser = it.obj
                            MyApplication.mUser?.nickname = MyApplication.mUser?.userName
                            MyApplication.mToken = it.msg
                            map[TOKEN] = it.msg
                            mLoginEvent.call()
                            val editor = MyApplication.mContext.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE).edit()
                            map.forEach {
                                editor.putString(it.key,it.value)
                            }
                            editor.apply()
                        }, {_,_ ->
                            toast(R.string.login_fail)
                        })
            }
        }
    }

}





