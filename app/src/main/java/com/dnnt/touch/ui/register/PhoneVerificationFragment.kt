package com.dnnt.touch.ui.register

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import com.dnnt.touch.R
import com.dnnt.touch.di.ActivityScoped
import com.dnnt.touch.receiver.NetworkReceiver
import com.dnnt.touch.ui.base.BaseFragment
import com.dnnt.touch.ui.resetpassword.NewPwdFragment
import com.dnnt.touch.util.*
import dagger.Lazy
import kotlinx.android.synthetic.main.fragment_phone_verification.*
import javax.inject.Inject

class PhoneVerificationFragment @Inject constructor() : BaseFragment<RegisterViewModel>() {

    @Inject lateinit var registerFragmentProvider: Lazy<RegisterFragment>
    @Inject lateinit var newPwdFragmentProvider: Lazy<NewPwdFragment>
    private var phone = ""
    private var code = ""
    private var codeTag = 0

    @SuppressLint("SetTextI18n")
    override fun init() {


        codeTag = arguments?.getInt(CODE_TAG,0) ?: 0

        //获取验证码，60s内不可再点击，防止多次发送验证码
        with(get_code){
            setOnClickListener {
                phone = user_phone.text.toString()
                if(phone.length != 11){
                    toast(R.string.phone_wrong)
                    return@setOnClickListener
                }
                if (NetworkReceiver.isNetUsable()){
                    countDown()
                    isClickable = false
                    text = "60"
                    mViewModel. getVerificationCode(phone,codeTag)
                }
            }
        }

        val bundle = Bundle()

        //订阅验证码验证事件
        mViewModel.mVerificationEvent.observe(this, Observer<Void> {
            code = verification_code.text.toString()
            mViewModel.codeVerification(phone,code)
            bundle.putString(PHONE,phone)
            bundle.putString(VERIFICATION_CODE,code)
        })


        //订阅验证码验证成功后的事件
        if (codeTag == CODE_TAG_REGISTER){
            mViewModel.mNextStepEvent.observe(this, Observer<String> {
                val fragment = registerFragmentProvider.get()
                fragment.arguments = bundle
                if(activity != null){
                    activity!!.supportFragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .hide(this)
                        .add(R.id.container, fragment)
                        .commit()
                }
            })
        }else{
            mViewModel.mNextStepEvent.observe(this, Observer {
                val fragment = newPwdFragmentProvider.get()
                fragment.arguments = bundle
                activity!!.supportFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .add(R.id.container,fragment)
                    .hide(this)
                    .commit()
            })
        }

        //点击下一步后，验证验证码是否正确
        next_step.setOnClickListener {
            mViewModel.mVerificationEvent.call()
        }

//        debugOnly {
//            user_phone.setText("18255132583")
//            get_code.performClick()
//        }


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
