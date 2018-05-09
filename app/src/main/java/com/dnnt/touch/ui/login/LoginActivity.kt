package com.dnnt.touch.ui.login

import android.arch.lifecycle.Observer
import com.dnnt.touch.ui.base.BaseActivity
import com.dnnt.touch.R
import com.dnnt.touch.ui.main.MainActivity
import com.dnnt.touch.ui.register.RegisterActivity
import com.dnnt.touch.ui.resetpassword.ResetPwdActivity
import com.dnnt.touch.base.DialogObserver
import com.dnnt.touch.util.*
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*


class LoginActivity : BaseActivity<LoginViewModel>(){

    override fun init() {

        //订阅登录成功事件
        mViewModel.mLoginEvent.observe(this, Observer<Void> {
            startActivity(MainActivity::class.java)
            finish()
        })

        login.setOnClickListener { loginClick() }

        go_register.setOnClickListener {
            startActivity(RegisterActivity::class.java)
        }

        go_reset_password.setOnClickListener {
            startActivity(ResetPwdActivity::class.java)
        }

//        debugOnly {
//            go_register.performClick()
//        }


        debugOnly {
            name_or_phone.setText("18255132583")
            password.setText("123456")
//            launch(UI) {
//                login.performClick()
//            }
            logi("LoginActivity",Date(System.currentTimeMillis()).toString())
        }

        val nameOrPhone = getString(NAME_OR_PHONE)
        if (!nameOrPhone.isEmpty()){
            name_or_phone.setText(nameOrPhone)
            password.setText(getString(PASSWORD))
        }

    }


    private fun loginClick() {
        val dialog = getProgressDialog(this, getString(R.string.login_ing))
        val observer = DialogObserver(dialog)
        mViewModel.mLoading.observe(this, observer)
        mViewModel.login(name_or_phone.text.toString(), password.text.toString())
    }

    override fun getLayoutId(): Int = R.layout.activity_login

    @Inject
    override fun setViewModel(viewModel: LoginViewModel) {
        mViewModel = viewModel
    }

}
