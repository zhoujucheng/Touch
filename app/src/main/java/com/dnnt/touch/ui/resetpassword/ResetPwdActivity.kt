package com.dnnt.touch.ui.resetpassword

import android.arch.lifecycle.Observer
import com.dnnt.touch.R
import com.dnnt.touch.ui.base.BaseActivity
import com.dnnt.touch.ui.register.PhoneVerificationFragment
import com.dnnt.touch.ui.register.RegisterViewModel
import dagger.Lazy
import javax.inject.Inject

class ResetPwdActivity : BaseActivity<RegisterViewModel>() {

    @Inject lateinit var phoneFragmentProvider: Lazy<PhoneVerificationFragment>
    @Inject lateinit var newPwdFragmentProvider: Lazy<NewPwdFragment>

    override fun init() {

        supportFragmentManager.beginTransaction()
                .add(R.id.container,phoneFragmentProvider.get())
                .commit()

        mViewModel.mNextStepEvent.observe(this, Observer {
            supportFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .add(R.id.container,newPwdFragmentProvider.get())
                    .hide(phoneFragmentProvider.get())
                    .commit()
        })

        mViewModel.mFinishEvent.observe(this,Observer {
            this.finish()
        })


    }

    override fun getLayoutId(): Int = R.layout.activity_reset_pwd

    @Inject override fun setViewModel(viewModel: RegisterViewModel) {
        mViewModel = viewModel
    }


}
