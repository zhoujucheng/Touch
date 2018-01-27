package com.dnnt.touch.ui.login

import com.dnnt.touch.BaseActivity
import com.dnnt.touch.R
import com.dnnt.touch.databinding.ActivityLoginBinding
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    @Inject
    lateinit var mLoginVM: LoginVM

    override fun init() {
        login_btn.setOnClickListener{ mLoginVM.login(user_name.text.toString(),pwd.text.toString()) }
    }

    override fun bindViewModel() {
        mDataBinding.viewModel = mLoginVM
    }

    override fun getLayoutId(): Int = R.layout.activity_login


}
