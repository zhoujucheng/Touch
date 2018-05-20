package com.dnnt.touch.ui.splash

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import com.dnnt.touch.MyApplication
import com.dnnt.touch.R
import com.dnnt.touch.been.User
import com.dnnt.touch.receiver.NetworkReceiver
import com.dnnt.touch.ui.login.LoginActivity
import com.dnnt.touch.ui.main.MainActivity
import com.dnnt.touch.util.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val sharedPre = getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE)
        val token = sharedPre.getString(TOKEN,"")
        launch(UI) {
            delay(1200,TimeUnit.MILLISECONDS)
            if (token.isEmpty()){
                startActivity(Intent(this@SplashActivity,LoginActivity::class.java))
            }else{
                startActivity(Intent(this@SplashActivity,MainActivity::class.java))
            }
            this@SplashActivity.finish()
        }

        if (token.isNotEmpty()){
            val id = sharedPre.getLong(ID,0L)
            val userName = sharedPre.getString(USER_NAME,"")
            val phone = sharedPre.getString(PHONE,"")
            val headUrl = sharedPre.getString(HEAD_URL,"/user/head/default.png")
            MyApplication.mToken = token
            MyApplication.mUser = User(id,0,userName,phone,headUrl,userName)
        }

        if (!NetworkReceiver.isNetUsable()){
            toastLong(R.string.network_not_available)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //创建通知渠道
            createNotificationChannel(CHANNEL_MSG_ID,getString(R.string.notification_msg_name),NotificationManager.IMPORTANCE_DEFAULT)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(id: String, name: String, importance: Int){
        val channel = NotificationChannel(id,name,importance)
        channel.setShowBadge(true)
        channel.enableLights(true)
        channel.lightColor = Color.BLUE
        channel.enableVibration(true)
        channel.setShowBadge(true)
        val manager = MyApplication.mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}
