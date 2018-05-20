package com.dnnt.touch.util

import android.util.Log
import okhttp3.OkHttpClient
import javax.inject.Inject

/**
 * Created by dnnt on 18-1-26.
 */


const val IP = "192.168.1.226"
//const val IP = "120.79.250.237"
const val PORT = "8443"
const val BASE_URL = "https://$IP:$PORT/touch/"



//  field
const val USER_NAME = "userName"
const val PHONE = "phone"
const val NAME_OR_PHONE = "nameOrPhone"
const val PASSWORD = "password"
const val VERIFICATION_CODE = "verificationCode"
const val COOKIE = "Cookie"
const val TOKEN = "token"
const val PRE_NAME = "touch"
const val ID = "id"
const val OLD_PASSWORD = "oldPassword"
const val NEW_PASSWORD = "newPassword"
const val HEAD_URL = "headUrl"

//msg type
const val TYPE_HEARTBEAT = 0
const val TYPE_MSG = 1
const val TYPE_CONNECTED = 2
const val TYPE_ACK = 4
const val TYPE_ADD_FRIEND = 8
const val TYPE_FRIEND_AGREE = 0x10
const val TYPE_USER_NOT_EXIST = 0x20
const val TYPE_USER_ALREADY_ADD = 0x40
const val TYPE_HEAD_UPDATE = 0X80
const val TYPE_SEND_FAIL = 0x100
const val TYPE_TOKEN_WRONG = 0x200
const val TYPE_OVERTIME = 0x400
const val TYPE_OTHER_LOGIN = 0x800
const val TYPE_UPDATE_USER_NAME = 0x1000


const val NAME_MIN_LENGTH = 4
const val NAME_MAX_LENGTH = 16
const val PWD_MAX_LEN = 16
const val PWD_MIN_LEN = 6

const val USER = "user"
const val CHAT_USER_ID = "chatUserId"
const val CODE_TAG_REGISTER = 1
const val CODE_TAG_RESET = 2
const val CODE_TAG = "codeTag"
const val CRASH_DIR = "crash"

const val SPLIT_CHAR = ';'

const val ACTIVITY_SET_HEAD_REQ = 1

const val READ_EXTERNAL_STORAGE = 1

//network
const val NO_NETWORK = 0
const val MOBILE_DATA = 1
const val WIFI = 2

//notification
const val CHANNEL_MSG_ID = "msg"
const val NOTIFICATION_MSG_ID = 1