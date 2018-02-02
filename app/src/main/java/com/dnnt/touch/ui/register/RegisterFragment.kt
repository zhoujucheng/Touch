package com.dnnt.touch.ui.register

import com.dnnt.touch.R
import com.dnnt.touch.di.ActivityScoped
import com.dnnt.touch.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_register.*
import javax.inject.Inject

@ActivityScoped
class RegisterFragment @Inject constructor() : BaseFragment<RegisterViewModel>() {

    override fun init() {
        register.setOnClickListener { mViewModel.register() }
    }

    override fun getLayoutId(): Int = R.layout.fragment_register

    @Inject
    override fun setViewModule(viewModel: RegisterViewModel) {
        mViewModel = viewModel
    }


}
