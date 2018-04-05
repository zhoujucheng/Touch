package com.dnnt.touch.util

import android.util.Log
import com.dnnt.touch.BuildConfig

/**
 * Created by dnnt on 18-3-21.
 * 该文件的函数用于debug
 * 函数必须是内联的(inline),这样能保证在其他地方调用该文件的函数时，调用处仅在DEBUG模式时被编译和执行
 */

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
