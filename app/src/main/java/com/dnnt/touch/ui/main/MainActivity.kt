package com.dnnt.touch.ui.main

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.PermissionRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dnnt.touch.MyApplication
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import com.dnnt.touch.R
import com.dnnt.touch.been.IMMsg
import com.dnnt.touch.been.LatestChat
import com.dnnt.touch.been.User
import com.dnnt.touch.been.User_Table
import com.dnnt.touch.netty.MsgHandler
import com.dnnt.touch.netty.NettyService
import com.dnnt.touch.protobuf.ChatProto
import com.dnnt.touch.receiver.NetworkReceiver
import com.dnnt.touch.ui.base.BaseActivity
import com.dnnt.touch.ui.login.LoginActivity
import com.dnnt.touch.ui.main.contact.ContactFragment
import com.dnnt.touch.ui.main.message.MessageFragment
import com.dnnt.touch.util.*
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.sql.language.From
import dagger.Lazy
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_add_friend.view.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel>(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        val TAG = "MainActivity"
    }

    @Inject lateinit var messageFragmentProvider: Lazy<MessageFragment>
    @Inject lateinit var contactFragmentProvider: Lazy<ContactFragment>
    @Inject lateinit var networkReceiver: NetworkReceiver

    override fun init() {
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val fragmentList = listOf<Lazy<out DaggerFragment>>(messageFragmentProvider, contactFragmentProvider)
        val pagerAdapter = MainPagerAdapter(supportFragmentManager,fragmentList)
        view_pager.adapter = pagerAdapter



        startService(Intent(this,NettyService::class.java))

//        launch {
//            delay(3000)
//            stopService(Intent(MyApplication.mContext,NettyService::class.java))
//        }
        debugOnly {
//            (select from IMMsg::class).list.forEach {
//                it.delete()
//            }
//            (select from LatestChat::class).list.forEach {
//                it.delete()
//            }
//            (select from User::class).list.forEach {
//                it.delete()
//            }
            launch(UI){
                if (MyApplication.mUser != null){
                    //TODO Have a better solutions?
                    while (user_head == null)   delay(100)
                    Glide.with(this@MainActivity).load(BASE_URL + MyApplication.mUser?.headUrl).into(user_head)
                    user_name.text = MyApplication.mUser?.userName ?: ""
                    user_head.setOnClickListener{
                        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            openAlbum()
                        } else {
                            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE)
                        }
                    }
                }
            }
        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            READ_EXTERNAL_STORAGE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAlbum()
            } else {
                toast(R.string.permission_denied)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ACTIVITY_SET_HEAD_REQUEST && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (uri != null){
                Glide.with(this)
                    .asBitmap()
                    .listener(object :  RequestListener<Bitmap> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                            return false
                        }
                        override fun onResourceReady(
                            resource: Bitmap?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            if (resource != null){
                                val headCache = File(cacheDir.path + "/head.png")
                                mViewModel.updateHead(resource, headCache)
                            }
                            return false
                        }
                    })
                    .load(uri)
                    .into(user_head)
            }
        }
    }

    fun openAlbum() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, ACTIVITY_SET_HEAD_REQUEST)
    }

    override fun getLayoutId() = R.layout.activity_main

    @Inject override fun setViewModel(viewModel: MainViewModel) {
        mViewModel = viewModel
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        stopService(Intent(this,NettyService::class.java))
        super.onDestroy()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.add_friend -> handleAddFriend()
            R.id.quit -> {
                startActivity(Intent(this,LoginActivity::class.java))
                this.finish()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun handleAddFriend(){
        val view = View.inflate(this,R.layout.dialog_add_friend,null)
        AlertDialog.Builder(this)
            .setView(view)
            .create()
            .show()
        with(view){
            btn_add_friend.setOnClickListener {
                val nameOrPhone = name_or_phone.text.toString()
                when {
                    isNameLegal(nameOrPhone) || nameOrPhone.matches(Regex("\\d{11}")) -> {
                        val user = MyApplication.mUser as User
                        val id = user.id
                        if(nameOrPhone == user.userName || nameOrPhone == user.phone){
                            toast(R.string.can_not_add_yourself)
                            return@setOnClickListener
                        }
                        MsgHandler.sendMsg(IMMsg(from = id,msg = nameOrPhone,type = TYPE_ADD_FRIEND))
                    }
                    else -> toast(R.string.user_not_exist)
                }
            }
        }
    }
}
