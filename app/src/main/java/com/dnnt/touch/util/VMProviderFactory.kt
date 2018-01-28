package com.dnnt.touch.util

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import javax.inject.Inject


/**
 * Created by dnnt on 18-1-28.
 */
class VMProviderFactory @Inject constructor() : ViewModelProvider.Factory {

    var mViewModel: ViewModel? = null

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isInstance(mViewModel)){
            return mViewModel as T
        }
        throw IllegalArgumentException("viewModel not matches modelClass")
    }
}