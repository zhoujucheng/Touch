package com.dnnt.touch.ui.register

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import com.dnnt.touch.R
import com.dnnt.touch.di.ActivityScoped
import com.dnnt.touch.receiver.NetworkReceiver
import com.dnnt.touch.ui.base.BaseFragment
import com.dnnt.touch.util.PHONE
import com.dnnt.touch.util.VERIFICATION_CODE
import com.dnnt.touch.util.debugOnly
import kotlinx.android.synthetic.main.fragment_phone_verification.*
import javax.inject.Inject

class PhoneVerificationFragment @Inject constructor() : BaseFragment<RegisterViewModel>() {

    @Inject lateinit var registerFragment: RegisterFragment
    private var phone = ""
    private var code = ""

    @SuppressLint("SetTextI18n")
    override fun init() {

        //获取验证码，60s内不可再点击，防止多次发送验证码
        with(get_code){
            setOnClickListener {
                if (NetworkReceiver.isNetUsable()){
                    countDown()
                    isClickable = false
                    text = "60"
                    phone = user_phone.text.toString()
                    mViewModel.getVerificationCode(phone)
                }
            }
        }


        //订阅验证码验证事件
        mViewModel.mVerificationEvent.observe(this, Observer<Void> {
            code = verification_code.text.toString()
            mViewModel.codeVerification(phone,code)
        })

        //订阅验证码验证成功后的事件
        mViewModel.mNextStepEvent.observe(this, Observer<String> {
            val bundle = Bundle()
            bundle.putString(PHONE,it)
            bundle.putString(VERIFICATION_CODE,code)
            registerFragment.arguments = bundle
            if(activity != null){
                activity!!.supportFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .hide(this)
                    .add(R.id.container, registerFragment)
                    .commit()
            }
        })

        //点击下一步后，验证验证码是否正确
        next_step.setOnClickListener {
            mViewModel.mVerificationEvent.call()
        }

        debugOnly {
            user_phone.setText("18255132583")
            get_code.performClick()
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
