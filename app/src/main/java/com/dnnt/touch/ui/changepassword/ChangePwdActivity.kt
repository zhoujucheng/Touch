package com.dnnt.touch.ui.changepassword

import com.dnnt.touch.R
import com.dnnt.touch.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_change_pwd.*
import kotlinx.android.synthetic.main.password_set.*
import javax.inject.Inject

class ChangePwdActivity : BaseActivity<ChangePwdViewModel>() {

    override fun init() {
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
