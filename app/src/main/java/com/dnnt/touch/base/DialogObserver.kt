package com.dnnt.touch.base

import android.app.AlertDialog
import android.arch.lifecycle.Observer

/**
 * Created by dnnt on 18-1-28.
 */
class DialogObserver(dialog: AlertDialog) : Observer<Boolean> {
    private var mDialog: AlertDialog? = dialog

    init {
        mDialog?.setOnDismissListener { mDialog = null }
    }

    override fun onChanged(t: Boolean?) {
        if (t == null) return
        if (t){
            mDialog?.show()
        }else{
            mDialog?.dismiss()
        }
    }
}