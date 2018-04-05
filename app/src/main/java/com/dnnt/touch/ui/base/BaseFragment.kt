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
//    必须在子类中进行注入
    protected lateinit var mViewModel: T

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater,getLayoutId(),container,false)
//        要求layout中类型为ViewModel子类的variable的name必须为viewModel
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
