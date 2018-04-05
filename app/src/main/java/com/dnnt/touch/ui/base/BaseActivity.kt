package com.dnnt.touch.ui.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import com.dnnt.touch.BR
import com.dnnt.touch.base.VMProviderFactory
import dagger.android.support.DaggerAppCompatActivity

/**
 * Created by dnnt on 17-12-23.
 */
abstract class BaseActivity<T: ViewModel> : DaggerAppCompatActivity() {

//    必须在子类中进行注入
    protected lateinit var mViewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val dataBinding: ViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
//        要求layout中类型为ViewModel子类的variable的name必须为viewModel
        dataBinding.setVariable(BR.viewModel,mViewModel)
        dataBinding.executePendingBindings()
    }

    fun startActivity(cls: Class<*>){
        startActivity(Intent(this,cls))
    }

    abstract fun init()

    abstract fun getLayoutId(): Int

    abstract fun setViewModel(viewModel: T)
}