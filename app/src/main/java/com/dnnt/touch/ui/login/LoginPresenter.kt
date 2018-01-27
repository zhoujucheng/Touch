package com.dnnt.touch.ui.login

import com.dnnt.touch.di.ActivityScoped
import javax.inject.Inject

/**
 * Created by dnnt on 18-1-25.
 */
@ActivityScoped
class LoginPresenter @Inject constructor() : LoginContract.Presenter {

    private var loginView: LoginContract.View? = null

    override fun bind(view: LoginContract.View) {
        loginView = view
    }

    override fun unbind() {
        loginView = null
    }

}