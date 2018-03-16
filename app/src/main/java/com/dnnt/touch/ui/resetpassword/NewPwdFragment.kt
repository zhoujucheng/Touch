package com.dnnt.touch.ui.resetpassword

import com.dnnt.touch.R
import com.dnnt.touch.ui.base.BaseFragment
import com.dnnt.touch.ui.register.RegisterViewModel
import kotlinx.android.synthetic.main.password_set.*
import javax.inject.Inject

class NewPwdFragment @Inject constructor() : BaseFragment<RegisterViewModel>() {

    override fun init() {

        complete.setOnClickListener {
//            TODO
//            mViewModel.resetPassword()
        }

    }

    override fun getLayoutId(): Int = R.layout.fragment_new_pwd

    @Inject
    override fun setViewModule(viewModel: RegisterViewModel) {
        mViewModel = viewModel
    }


}
