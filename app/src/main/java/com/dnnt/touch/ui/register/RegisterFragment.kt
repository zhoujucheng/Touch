package com.dnnt.touch.ui.register

import com.dnnt.touch.R
import com.dnnt.touch.ui.base.BaseFragment
import com.dnnt.touch.base.DialogObserver
import com.dnnt.touch.util.getProgressDialog
import kotlinx.android.synthetic.main.password_set.*
import javax.inject.Inject

class RegisterFragment @Inject constructor() : BaseFragment<RegisterViewModel>() {

    override fun init() {

        password_layout.hint = getString(R.string.hint_register_password)
        password1_layout.hint = getString(R.string.user_password_again)
        complete.setText(R.string.register)

        complete.setOnClickListener {
//            TODO
//            mViewModel.register()
        }

    }

    fun registerClick(){
        val dialog = getProgressDialog(this.context!!,getString(R.string.registering))
        val observer = DialogObserver(dialog)
        mViewModel.mLoading.observe(this,observer)
    }

    override fun getLayoutId(): Int = R.layout.fragment_register

    @Inject override fun setViewModule(viewModel: RegisterViewModel) {
        mViewModel = viewModel
    }


}
