package com.dnnt.touch.ui.changepassword

import android.content.Context
import com.dnnt.touch.MyApplication
import com.dnnt.touch.R
import com.dnnt.touch.base.SingleLiveEvent
import com.dnnt.touch.network.NetService
import com.dnnt.touch.ui.base.BaseViewModel
import com.dnnt.touch.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by dnnt on 18-5-6.
 */
class ChangePwdViewModel @Inject constructor() : BaseViewModel() {
    companion object {
        val TAG = "ChangePwdViewModel"
    }


    @Inject lateinit var netService: NetService
    val successEven = SingleLiveEvent<Void>()

    fun changePassword(oldPassword: String, newPassword: String, newPassword1: String){
        when{
            oldPassword.length !in PWD_MIN_LEN..PWD_MAX_LEN -> toast(R.string.wrong_password)
            newPassword.length !in PWD_MIN_LEN..PWD_MAX_LEN -> toast(R.string.pwd_len_wrong)
            newPassword != newPassword1 -> toast(R.string.password_not_equal)
            else -> {
                val id = MyApplication.mUser?.id ?: 0
                netService.changePassword(hashMapOf(Pair(ID,id.toString()), Pair(OLD_PASSWORD,oldPassword),
                    Pair(NEW_PASSWORD,newPassword)))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        removeSensitiveInfo(MyApplication.mContext)
                        toast(R.string.change_password_success)
                        successEven.call()
                    },{msg,_ ->
                        toast(msg)
                    })
            }
        }
    }
}