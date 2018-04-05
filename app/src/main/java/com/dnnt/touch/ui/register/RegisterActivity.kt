package com.dnnt.touch.ui.register

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.dnnt.touch.R
import com.dnnt.touch.ui.base.BaseActivity
import com.dnnt.touch.util.PHONE
import javax.inject.Inject
import dagger.Lazy

class RegisterActivity : BaseActivity<RegisterViewModel>() {

    @Inject lateinit var phoneFragment: PhoneVerificationFragment
//    @Inject lateinit var registerFragment: RegisterFragment

//    @Inject lateinit var phoneFragmentProvider: Lazy<PhoneVerificationFragment>
//    @Inject lateinit var registerFragmentProvider: Lazy<RegisterFragment>

    override fun init() {

        supportFragmentManager.beginTransaction()
                .add(R.id.container, phoneFragment)
                .commit()

//        //订阅验证码验证成功后的事件
//        mViewModel.mNextStepEvent.observe(this, Observer<String> {
//            val bundle = Bundle()
//            bundle.putString(PHONE,it)
//            registerFragment.arguments = bundle
//            supportFragmentManager.beginTransaction()
//                    .addToBackStack(null)
//                    .hide(phoneFragment)
//                    .add(R.id.container, registerFragment)
//                    .commit()
//        })

        //订阅所注册完成后的事件
        mViewModel.mFinishEvent.observe(this,Observer<Void>{
            this.finish()
        })

    }

    override fun getLayoutId(): Int = R.layout.activity_register

    @Inject override fun setViewModel(viewModel: RegisterViewModel) {
        mViewModel = viewModel
    }

}
