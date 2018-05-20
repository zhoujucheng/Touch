package com.dnnt.touch.ui.changepassword

import android.app.Activity
import android.arch.lifecycle.Observer
import com.dnnt.touch.MyApplication
import com.dnnt.touch.R
import com.dnnt.touch.ui.base.BaseActivity
import com.dnnt.touch.ui.login.LoginActivity
import com.dnnt.touch.util.finishAllActivity
import com.dnnt.touch.util.loge
import com.dnnt.touch.util.logi
import kotlinx.android.synthetic.main.activity_change_pwd.*
import kotlinx.android.synthetic.main.password_set.*
import javax.inject.Inject

class ChangePwdActivity : BaseActivity<ChangePwdViewModel>() {

    override fun init() {
        mViewModel.successEven.observe(this, Observer {
            startActivity(LoginActivity::class.java)
            finishAllActivity()
        })
        complete.setOnClickListener {
            mViewModel.changePassword(oldPassword.text.toString(),password.text.toString(),password1.text.toString())
        }
    }

    override fun getLayoutId() = R.layout.activity_change_pwd

    @Inject
    override fun setViewModel(viewModel: ChangePwdViewModel) {
        mViewModel = viewModel
    }

}
