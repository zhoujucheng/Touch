package com.dnnt.touch.ui.register

import android.content.Context
import com.dnnt.touch.R
import com.dnnt.touch.ui.login.base.BaseActivity
import javax.inject.Inject

class RegisterActivity : BaseActivity<RegisterViewModel>() {


    override fun init() {


    }

    override fun getLayoutId(): Int = R.layout.activity_register

    @Inject
    override fun setViewModel(viewModel: RegisterViewModel) {
        mViewModel = viewModel
    }

}
