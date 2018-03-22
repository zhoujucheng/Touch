package com.dnnt.touch.util

import android.util.Log
import com.dnnt.touch.BuildConfig


/**
 * Created by dnnt on 18-3-21.
 */

//当且仅当是DEBUG模式时才编译和执行
inline fun debugOnly(runDebug: () -> Unit){
    if (BuildConfig.DEBUG) {
        runDebug()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun logv(tag: String, msg: String?){
    debugOnly {
        val str = msg ?: "msg is null"
        Log.v(tag, str)
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun logd(tag: String, msg: String?){
    debugOnly {
        val str = msg ?: "msg is null"
        Log.d(tag, str)
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun logi(tag: String, msg: String?){
    debugOnly {
        val str = msg ?: "msg is null"
        Log.i(tag, str)
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun logw(tag: String, msg: String?){
    debugOnly {
        val str = msg ?: "msg is null"
        Log.w(tag, str)
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun loge(tag: String, msg: String?){
    debugOnly {
        val str = msg ?: "msg is null"
        Log.e(tag, str)
    }
}
