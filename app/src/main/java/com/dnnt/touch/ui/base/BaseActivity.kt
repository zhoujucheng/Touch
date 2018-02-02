package com.dnnt.touch.ui.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
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
        dataBinding()
        provideViewModel()
        init()
    }

    private fun provideViewModel(){
        val factory = VMProviderFactory()
        factory.mViewModel = mViewModel
        ViewModelProviders.of(this, factory).get(mViewModel.javaClass)
    }

    private fun dataBinding(){
        val dataBinding: ViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        val method = dataBinding.javaClass.getDeclaredMethod("setViewModel", mViewModel.javaClass)
        method.invoke(dataBinding, mViewModel)
    }

    fun startActivity(cls: Class<*>){
        startActivity(Intent(this,cls))
    }

    abstract fun init()

    abstract fun getLayoutId(): Int

    abstract fun setViewModel(viewModel: T)
}