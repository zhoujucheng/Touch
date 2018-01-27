package com.dnnt.touch.been

/**
 * Created by dnnt on 18-1-25.
 */
data class Json<T>( var msg: String = "", var successful: Boolean = false, var obj: T? = null, var code: Int = -1 )
