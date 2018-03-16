package com.dnnt.touch.been

/**
 * Created by dnnt on 18-1-25.
 */
data class User(val id: Long, val userName: String, val phone: String, val password: String = "",
                val token: String = "", val headUrl: String?, val nickname: String?)