package com.dnnt.touch.ui.resetpassword

import com.dnnt.touch.R
import com.dnnt.touch.ui.base.BaseFragment
import com.dnnt.touch.ui.register.RegisterViewModel
import com.dnnt.touch.util.PHONE
import com.dnnt.touch.util.VERIFICATION_CODE
import kotlinx.android.synthetic.main.password_set.*
import javax.inject.Inject

class NewPwdFragment @Inject constructor() : BaseFragment<RegisterViewModel>() {

    override fun init() {
        
        val phone = arguments?.getString(PHONE) ?: ""
        val code = arguments?.getString(VERIFICATION_CODE) ?: ""

        complete.setOnClickListener {
            mViewModel.resetPassword(password.text.toString(),password1.text.toString(),phone,code)
        }

    }

    override fun getLayoutId(): Int = R.layout.fragment_new_pwd

    @Inject override fun setViewModule(viewModel: RegisterViewModel) {
        mViewModel = viewModel
    }


}
