package com.dnnt.touch.ui.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import com.dnnt.touch.BR
import com.dnnt.touch.MyApplication
import com.dnnt.touch.base.VMProviderFactory
import com.dnnt.touch.been.User
import com.dnnt.touch.util.USER
import com.dnnt.touch.util.logi
import dagger.android.support.DaggerAppCompatActivity

/**
 * Created by dnnt on 17-12-23.
 */
abstract class BaseActivity<T: ViewModel> : DaggerAppCompatActivity() {

//    必须在子类中进行注入
    lateinit var mViewModel: T
    protected lateinit var mDataBinding: ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val user = savedInstanceState?.getSerializable(USER) as? User
//        if(user != null){
//            MyApplication.mUser = user
//        }
//        logi("Application User",user?.userName ?: "NULL")
        dataBinding()
        provideViewModelFactory()
        init()
    }


    private fun provideViewModelFactory(){
        val factory = VMProviderFactory()
        factory.mViewModel = mViewModel
        ViewModelProviders.of(this, factory).get(mViewModel.javaClass)
    }

    private fun dataBinding(){
        mDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
//        要求layout中类型为ViewModel子类的variable的name必须为viewModel
        mDataBinding.setVariable(BR.viewModel,mViewModel)
        mDataBinding.executePendingBindings()
    }

    fun startActivity(cls: Class<*>){
        startActivity(Intent(this,cls))
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        if(MyApplication.mUser != null){
//            outState.putSerializable(USER,MyApplication.mUser)
//        }
//        logi("Application User IsSave","true")
//        super.onSaveInstanceState(outState)
//    }


    abstract fun init()

    abstract fun getLayoutId(): Int

    abstract fun setViewModel(viewModel: T)
}