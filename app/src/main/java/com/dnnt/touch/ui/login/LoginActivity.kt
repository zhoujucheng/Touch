package com.dnnt.touch.ui.login

import android.arch.lifecycle.Observer
import com.dnnt.touch.ui.base.BaseActivity
import com.dnnt.touch.R
import com.dnnt.touch.ui.register.RegisterActivity
import com.dnnt.touch.util.DialogObserver
import com.dnnt.touch.util.getProgressDialog
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity<LoginViewModel>(){

    override fun init() {
        mViewModel.mLoginEvent.observe(this, Observer<Void> { openMainActivity() })
        login_btn.setOnClickListener{ loginClick() }
        go_register.setOnClickListener { startActivity(RegisterActivity::class.java) }
        go_reset_pwd.setOnClickListener {  }
    }

    fun openMainActivity() {
//        Log.e(TAG, "openMainActivity")
//        TODO("not implemented") To change body of created functions use File | Settings | File Templates.
    }

    fun loginClick(){
        val dialog = getProgressDialog(this,getString(R.string.login_ing))
        val observer = DialogObserver(dialog)
        mViewModel.mLoading.observe(this,observer)
        mViewModel.login(user.text.toString(),pwd.text.toString())
    }

    override fun getLayoutId(): Int = R.layout.activity_login

    @Inject
    override fun setViewModel(viewModel: LoginViewModel){
        mViewModel = viewModel
    }

}
