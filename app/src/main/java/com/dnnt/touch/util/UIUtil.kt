package com.dnnt.touch.util

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.Toast
import com.dnnt.touch.MyApplication
import com.dnnt.touch.R

/**
 * Created by dnnt on 18-1-27.
 */


fun toast(msg: String) {
    Toast.makeText(MyApplication.mContext, msg, Toast.LENGTH_SHORT).show()
}

fun toast(resId: Int){
    Toast.makeText(MyApplication.mContext, resId, Toast.LENGTH_SHORT).show()
}

fun toast(resId: Int,vararg any: Any){
    Toast.makeText(MyApplication.mContext, String.format(getString(resId),any),Toast.LENGTH_SHORT).show()
}

fun toastLong(msg: String){
    Toast.makeText(MyApplication.mContext, msg, Toast.LENGTH_LONG).show()
}

fun toastLong(resId: Int){
    Toast.makeText(MyApplication.mContext, resId, Toast.LENGTH_LONG).show()
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

