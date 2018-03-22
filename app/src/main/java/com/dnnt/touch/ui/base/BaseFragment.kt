package com.dnnt.touch.ui.base


import android.arch.lifecycle.ViewModel
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import dagger.android.support.DaggerFragment


/**
 * A base [Fragment] class.
 */
abstract class BaseFragment<T: ViewModel> : DaggerFragment() {

    protected lateinit var mViewModel: T

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding: ViewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater,getLayoutId(),container,false)
        dataBinding.setVariable(BR.viewModel,mViewModel)
        dataBinding.executePendingBindings()
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    abstract fun init()

    abstract fun getLayoutId(): Int

    abstract fun setViewModule(viewModel: T)

}
