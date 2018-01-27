package com.dnnt.touch

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import dagger.android.DaggerActivity

/**
 * Created by dnnt on 17-12-23.
 */
abstract class BaseActivity<T: ViewDataBinding> : DaggerActivity() {

    protected lateinit var mDataBinding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        bindViewModel()
        init()
    }

    abstract fun init()

    abstract fun getLayoutId(): Int

    abstract fun bindViewModel()

}