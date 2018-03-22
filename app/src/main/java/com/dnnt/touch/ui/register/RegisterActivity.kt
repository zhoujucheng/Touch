package com.dnnt.touch.ui.register

import android.arch.lifecycle.Observer
import com.dnnt.touch.R
import com.dnnt.touch.ui.base.BaseActivity
import javax.inject.Inject
import dagger.Lazy

class RegisterActivity : BaseActivity<RegisterViewModel>() {

    @Inject lateinit var phoneFragmentProvider: Lazy<PhoneVerificationFragment>
    @Inject lateinit var registerFragmentProvider: Lazy<RegisterFragment>

    override fun init() {

        supportFragmentManager.beginTransaction()
                .add(R.id.container, phoneFragmentProvider.get())
                .commit()

        mViewModel.mNextStepEvent.observe(this, Observer<Void> {
            supportFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .hide(phoneFragmentProvider.get())
                    .add(R.id.container, registerFragmentProvider.get())
                    .commit()
        })

        mViewModel.mFinishEvent.observe(this,Observer<Void>{
            this.finish()
        })

    }

    override fun getLayoutId(): Int = R.layout.activity_register

    @Inject override fun setViewModel(viewModel: RegisterViewModel) {
        mViewModel = viewModel
    }

}
