package com.dnnt.touch.ui.login

import android.arch.lifecycle.Observer
import com.dnnt.touch.ui.base.BaseActivity
import com.dnnt.touch.R
import com.dnnt.touch.ui.main.MainActivity
import com.dnnt.touch.ui.register.RegisterActivity
import com.dnnt.touch.ui.resetpassword.ResetPwdActivity
import com.dnnt.touch.util.DialogObserver
import com.dnnt.touch.util.getProgressDialog
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity<LoginViewModel>(){

    override fun init() {
        mViewModel.mLoginEvent.observe(this, Observer<Void> {
            startActivity(MainActivity::class.java)
        })

        login.setOnClickListener{ loginClick() }

        go_register.setOnClickListener {
            startActivity(RegisterActivity::class.java)
        }

        go_reset_password.setOnClickListener {
            startActivity(ResetPwdActivity::class.java)
        }

    }

    private fun loginClick(){
        val dialog = getProgressDialog(this,getString(R.string.login_ing))
        val observer = DialogObserver(dialog)
        mViewModel.mLoading.observe(this,observer)
        mViewModel.login(user_phone.text.toString(),password.text.toString())
    }

    override fun getLayoutId(): Int = R.layout.activity_login

    @Inject
    override fun setViewModel(viewModel: LoginViewModel){
        mViewModel = viewModel
    }

}
