package com.dnnt.touch.ui.register

import android.arch.lifecycle.Observer
import android.view.View
import com.dnnt.touch.R
import com.dnnt.touch.di.ActivityScoped
import com.dnnt.touch.network.NetService
import com.dnnt.touch.receiver.NetworkReceiver
import com.dnnt.touch.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_phone_verification.*
import javax.inject.Inject

@ActivityScoped
class PhoneVerificationFragment @Inject constructor() : BaseFragment<RegisterViewModel>() {

    override fun init() {

        mViewModel.mNextStepEvent.observe(activity, Observer<Void> {
            mViewModel.codeVerification(verification_code.text.toString())
        })

        next_step.setOnClickListener { mViewModel.mNextStepEvent.call() }

        get_code.setOnClickListener {
            if (NetworkReceiver.isNetUsable()){
                mViewModel.getVerificationCode(phone.text.toString())
                it.isClickable = false
                mViewModel.getCodeText.set("60")
                countDown(it)
            }
        }

    }

    private fun countDown(v: View){
        if (activity == null || activity.isFinishing){
            return
        }
        v.postDelayed({
            val left = mViewModel.getCodeText.get().toInt() - 1
            if (left <= 0){
                mViewModel.getCodeText.set(getString(R.string.get_verification_code))
                v.isClickable = true
            }else{
                countDown(v)
                mViewModel.getCodeText.set(left.toString())
            }
        },1000)
    }

    override fun getLayoutId(): Int = R.layout.fragment_phone_verification

    @Inject
    override fun setViewModule(viewModel: RegisterViewModel) {
        mViewModel = viewModel
    }
}
