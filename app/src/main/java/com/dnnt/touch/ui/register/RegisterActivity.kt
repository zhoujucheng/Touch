package com.dnnt.touch.ui.register

import android.arch.lifecycle.Observer
import com.dnnt.touch.R
import com.dnnt.touch.ui.base.BaseActivity
import javax.inject.Inject

class RegisterActivity : BaseActivity<RegisterViewModel>() {

    @Inject
    lateinit var phoneFragmentProvider: dagger.Lazy<PhoneVerificationFragment>
    @Inject
    lateinit var registerFragmentProvider: dagger.Lazy<RegisterFragment>

    override fun init() {

        supportFragmentManager.beginTransaction()
                .add(R.id.container, phoneFragmentProvider.get())
                .commit()

        mViewModel.mGoRegisterEvent.observe(this, Observer<Void> {
            supportFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .hide(phoneFragmentProvider.get())
                    .add(R.id.container, registerFragmentProvider.get())
                    .commit()
        })

    }

    override fun getLayoutId(): Int = R.layout.activity_register

    @Inject
    override fun setViewModel(viewModel: RegisterViewModel) {
        mViewModel = viewModel
    }

}
