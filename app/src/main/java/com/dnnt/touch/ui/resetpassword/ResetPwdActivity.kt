package com.dnnt.touch.ui.resetpassword

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.dnnt.touch.R
import com.dnnt.touch.ui.base.BaseActivity
import com.dnnt.touch.ui.register.PhoneVerificationFragment
import com.dnnt.touch.ui.register.RegisterViewModel
import com.dnnt.touch.util.CODE_TAG
import com.dnnt.touch.util.CODE_TAG_REGISTER
import com.dnnt.touch.util.CODE_TAG_RESET
import dagger.Lazy
import javax.inject.Inject

class ResetPwdActivity : BaseActivity<RegisterViewModel>() {

    @Inject lateinit var phoneFragmentProvider: Lazy<PhoneVerificationFragment>


    override fun init() {

        val bundle = Bundle()
        bundle.putInt(CODE_TAG, CODE_TAG_RESET)
        val fragment = phoneFragmentProvider.get()
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
                .add(R.id.container,fragment)
                .commit()

        //订阅验证码验证成功后的事件
//        mViewModel.mNextStepEvent.observe(this, Observer {
//            supportFragmentManager.beginTransaction()
//                    .addToBackStack(null)
//                    .add(R.id.container,newPwdFragmentProvider.get())
//                    .hide(phoneFragmentProvider.get())
//                    .commit()
//        })

        //订阅所注册完成后的事件
        mViewModel.mFinishEvent.observe(this,Observer {
            this.finish()
        })


    }

    override fun getLayoutId(): Int = R.layout.activity_reset_pwd

    @Inject override fun setViewModel(viewModel: RegisterViewModel) {
        mViewModel = viewModel
    }


}
