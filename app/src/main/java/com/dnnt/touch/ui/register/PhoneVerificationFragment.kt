package com.dnnt.touch.ui.register

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import com.dnnt.touch.R
import com.dnnt.touch.di.ActivityScoped
import com.dnnt.touch.receiver.NetworkReceiver
import com.dnnt.touch.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_phone_verification.*
import javax.inject.Inject

class PhoneVerificationFragment @Inject constructor() : BaseFragment<RegisterViewModel>() {

    @SuppressLint("SetTextI18n")
    override fun init() {

        mViewModel.mVerificationEvent.observe(this, Observer<Void> {
            mViewModel.codeVerification(verification_code.text.toString())
        })

        next_step.setOnClickListener { mViewModel.mVerificationEvent.call() }

        with(get_code){
            setOnClickListener {
                if (NetworkReceiver.isNetUsable()){
                    countDown()
                    isClickable = false
                    text = "60"
                    mViewModel.getVerificationCode(phone.text.toString())
                }
            }
        }
    }

    private fun countDown(){
        if (activity == null || activity!!.isFinishing){
            return
        }
        with(get_code){
            postDelayed({
                val left = text.toString().toInt() - 1
                if (left <= 0){
                    setText(R.string.get_verification_code)
                    isClickable = true
                }else{
                    countDown()
                    text = left.toString()
                }
            },1000)
        }

    }

    override fun getLayoutId(): Int = R.layout.fragment_phone_verification

    @Inject override fun setViewModule(viewModel: RegisterViewModel) {
        mViewModel = viewModel
    }
}
