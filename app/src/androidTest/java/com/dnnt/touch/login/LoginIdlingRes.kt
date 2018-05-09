package com.dnnt.touch.login

import android.support.test.espresso.IdlingResource
import com.dnnt.touch.ui.login.LoginActivity

/**
 * Created by dnnt on 18-5-7.
 */
class LoginIdlingRes(private val loginActivity: LoginActivity) : IdlingResource {
    var mCallBack: IdlingResource.ResourceCallback? = null
    companion object {
        val TAG = "LoginIdlingRes"
    }

    override fun getName() = TAG

    override fun isIdleNow(): Boolean {
        val isIdle = loginActivity.isFinishing || loginActivity.mViewModel.mLoading.value == false
        println(isIdle)
        if (isIdle){
            mCallBack?.onTransitionToIdle()
        }
        return isIdleNow
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        mCallBack = callback
    }
}