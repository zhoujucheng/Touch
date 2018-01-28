package com.dnnt.touch.ui.login

import com.dnnt.touch.ui.login.base.BaseActivity
import com.dnnt.touch.R
import com.dnnt.touch.databinding.ActivityLoginBinding
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity<LoginViewModel>(){

    override fun init() {
        login_btn.setOnClickListener{ mViewModel.login(user_name.text.toString(),pwd.text.toString()) }
    }

    fun openMainActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutId(): Int = R.layout.activity_login

    @Inject
    override fun setViewModel(viewModel: LoginViewModel){
        mViewModel = viewModel
    }

}
