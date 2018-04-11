package com.dnnt.touch.util

import android.util.Log
import okhttp3.OkHttpClient
import javax.inject.Inject

/**
 * Created by dnnt on 18-1-26.
 */

//const val BASE_URL = "http://120.79.250.237:8080/touch/"
const val IP = "192.168.1.226"
const val PORT = "8080"
const val BASE_URL = "http://$IP:$PORT/touch/"
const val USER = "user"
const val CHAT_USER_ID = "chatUserId"
//  field
const val USER_NAME = "userName"
const val PHONE = "phone"
const val NAME_OR_PHONE = "nameOrPhone"
const val PASSWORD = "password"
const val VERIFICATION_CODE = "verificationCode"
const val COOKIE = "Cookie"

const val TYPE_MSG = 1
const val TYPE_CONNECTED = 2
const val TYPE_ACK = 4
const val TYPE_ADD_FRIEND = 8
const val TYPE_FRIEND_AGREE = 0x10
const val TYPE_USER_NOT_EXIST = 0x20
const val TYPE_OVERTIME = 0x40

const val NAME_MIN_LENGTH = 4
const val NAME_MAX_LENGTH = 16

const val SPLIT_CHAR = ';'