package com.dnnt.touch.ui.login.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import com.dnnt.touch.util.VMProviderFactory
import dagger.android.support.DaggerAppCompatActivity

/**
 * Created by dnnt on 17-12-23.
 */
abstract class BaseActivity<T: ViewModel> : DaggerAppCompatActivity() {

    protected lateinit var mViewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataBinding: ViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        provideViewModel()
        bindViewModel(dataBinding)
        init()
    }

    private fun provideViewModel(){
        val factory = VMProviderFactory()
        factory.mViewModel = mViewModel
        ViewModelProviders.of(this, factory).get(mViewModel.javaClass)
    }

    private fun <V: ViewDataBinding> bindViewModel(dataBinding: V){
        val method = dataBinding.javaClass.getDeclaredMethod("setViewModel", mViewModel.javaClass)
        method.invoke(dataBinding, mViewModel)
    }


    abstract fun init()

    abstract fun getLayoutId(): Int

    abstract fun setViewModel(viewModel: T)
}