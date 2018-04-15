package com.dnnt.touch.util

import android.content.Context
import com.dnnt.touch.MyApplication

/**
 * Created by dnnt on 18-1-27.
 */

fun getString(resId: Int) = MyApplication.mContext.getString(resId)!!

fun getString(resId:Int,vararg any: Any) = MyApplication.mContext.getString(resId,any)

fun getString(name: String) = MyApplication.mContext.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE).getString(name,"")

fun isNameLegal(name: String): Boolean{
    if (name.length in NAME_MIN_LENGTH..NAME_MAX_LENGTH){
        if (!name.matches(Regex("\\d.*"))){
            return  true
        }
    }
    return false
}
