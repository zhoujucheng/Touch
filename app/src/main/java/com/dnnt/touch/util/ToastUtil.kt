package com.dnnt.touch.util

import android.widget.Toast
import com.dnnt.touch.MyApplication

/**
 * Created by dnnt on 18-1-27.
 */


fun toast(msg: String) {
    Toast.makeText(MyApplication.mContext, msg, Toast.LENGTH_SHORT).show()
}

fun toast(resId: Int){
    Toast.makeText(MyApplication.mContext, resId, Toast.LENGTH_SHORT).show()
}

fun toastLong(msg: String){
    Toast.makeText(MyApplication.mContext, msg, Toast.LENGTH_LONG).show()
}

fun toastLong(resId: Int){
    Toast.makeText(MyApplication.mContext, resId, Toast.LENGTH_LONG).show()
}
