package com.dnnt.touch.util

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.Toast
import com.dnnt.touch.MyApplication
import com.dnnt.touch.R
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * Created by dnnt on 18-1-27.
 */


fun toast(msg: String) {
    launch(UI) {
        Toast.makeText(MyApplication.mContext, msg, Toast.LENGTH_SHORT).show()
    }
}

fun toast(resId: Int){
    launch(UI) {
        Toast.makeText(MyApplication.mContext, resId, Toast.LENGTH_SHORT).show()
    }
}

fun toast(resId: Int,vararg strs: String){
    launch(UI) {
        Toast.makeText(MyApplication.mContext, String.format(getString(resId),*strs),Toast.LENGTH_SHORT).show()
    }
}

fun toastLong(msg: String){
    launch(UI) {
        Toast.makeText(MyApplication.mContext, msg, Toast.LENGTH_LONG).show()
    }
}

fun toastLong(resId: Int){
    launch(UI) {
        Toast.makeText(MyApplication.mContext, resId, Toast.LENGTH_LONG).show()
    }
}

fun getProgressDialog(context: Context, title:String = "") =
        AlertDialog.Builder(context)
                .setView(View.inflate(context,R.layout.loading_progress,null))
                .setTitle(title)
                .setCancelable(false)
                .create()

fun getDialog(context: Context, title: String = "", content: String = "") =
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(content)
                .create()

fun getDialog(context: Context, view: View) =
        AlertDialog.Builder(context)
                .setView(view)
                .create()

