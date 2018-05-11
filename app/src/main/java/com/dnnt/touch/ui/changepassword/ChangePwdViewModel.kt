package com.dnnt.touch.ui.changepassword

import android.content.Context
import com.dnnt.touch.MyApplication
import com.dnnt.touch.R
import com.dnnt.touch.network.NetService
import com.dnnt.touch.ui.base.BaseViewModel
import com.dnnt.touch.util.*
import javax.inject.Inject

/**
 * Created by dnnt on 18-5-6.
 */
class ChangePwdViewModel @Inject constructor() : BaseViewModel() {
    companion object {
        val TAG = "ChangePwdViewModel"
    }

    @Inject lateinit var netService: NetService

    fun changePassword(oldPassword: String, newPassword: String, newPassword1: String){
        when{
            oldPassword.length !in PWD_MIN_LEN..PWD_MAX_LEN -> toast(R.string.wrong_password)
            newPassword.length !in PWD_MIN_LEN..PWD_MAX_LEN -> toast(R.string.pwd_len_wrong)
            newPassword != newPassword1 -> toast(R.string.password_not_equal)
            else -> {
                val id = MyApplication.mUser?.id ?: 0
                netService.changePassword(hashMapOf(Pair(ID,id.toString()), Pair(OLD_PASSWORD,oldPassword),
                    Pair(NEW_PASSWORD,newPassword)))
                    .subscribe({
                        val editor = MyApplication.mContext.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE).edit()
                        editor.remove(PASSWORD)
                        editor.remove(TOKEN)
                        editor.apply()
                        toast(R.string.change_password_success)
                    },{msg,_ ->
                        toast(msg)
                    })
            }
        }
    }
}