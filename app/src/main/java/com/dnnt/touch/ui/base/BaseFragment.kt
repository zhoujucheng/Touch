package com.dnnt.touch.ui.base


import android.arch.lifecycle.ViewModel
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment


/**
 * A simple [Fragment] subclass.
 */
abstract class BaseFragment<T: ViewModel> : DaggerFragment() {

    protected lateinit var mViewModel: T

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val dataBinding: ViewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater,getLayoutId(),container,false)
        val method = dataBinding.javaClass.getDeclaredMethod("setViewModel", mViewModel.javaClass)
        method.invoke(dataBinding, mViewModel)

        return dataBinding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        init()
        super.onActivityCreated(savedInstanceState)
    }

    abstract fun init()

    abstract fun getLayoutId(): Int

    abstract fun setViewModule(viewModel: T)

}
