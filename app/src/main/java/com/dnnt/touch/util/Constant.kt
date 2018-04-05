package com.dnnt.touch.util

import android.util.Log
import okhttp3.OkHttpClient
import javax.inject.Inject

/**
 * Created by dnnt on 18-1-26.
 */

//const val BASE_URL = "http://120.79.250.237:8080/touch/"
const val IP = "192.168.186.226"
const val PORT = "8080"
const val BASE_URL = "http://$IP:$PORT/touch/"
//  field
const val USER_NAME = "userName"
const val PHONE = "phone"
const val NAME_OR_PHONE = "nameOrPhone"
const val PASSWORD = "password"
const val VERIFICATION_CODE = "verificationCode"
const val COOKIE = "Cookie"

