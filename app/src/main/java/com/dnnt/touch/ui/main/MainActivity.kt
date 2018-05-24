package com.dnnt.touch.ui.main

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import android.view.View
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
import com.dnnt.touch.been.User
import com.dnnt.touch.netty.MsgHandler
import com.dnnt.touch.netty.NettyService
import com.dnnt.touch.receiver.NetworkReceiver
import com.dnnt.touch.ui.base.BaseActivity
import com.dnnt.touch.ui.changepassword.ChangePwdActivity
import com.dnnt.touch.ui.login.LoginActivity
import com.dnnt.touch.ui.main.contact.ContactFragment
import com.dnnt.touch.ui.main.message.LatestChatFragment
import com.dnnt.touch.util.*
import dagger.Lazy
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_text_btn.view.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.io.File
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel>(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        val TAG = "MainActivity"
    }

    @Inject lateinit var latestChatFragmentProvider: Lazy<LatestChatFragment>
    @Inject lateinit var contactFragmentProvider: Lazy<ContactFragment>
    @Inject lateinit var networkReceiver: NetworkReceiver
    private lateinit var netChangeListener: NetworkReceiver.NetworkChangeListener

    override fun init() {
        if(NetworkReceiver.isNetUsable()){
            startService(Intent(this,NettyService::class.java))
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        mViewModel.userNameLiveData.observe(this, Observer {
            user_name.text = it ?: ""
        })

        initHeaderLayout()
        initViewPager()
    }

    private fun initHeaderLayout(){
        launch(UI){
            if (MyApplication.mUser != null){
                //TODO Have a better solutions(user_head may not have init)?
                while (user_head == null)   delay(100)
                Glide.with(this@MainActivity).load(BASE_URL + MyApplication.mUser?.headUrl).into(user_head)
                mViewModel.userNameLiveData.value = MyApplication.mUser?.userName
                user_name.setOnClickListener {
                    handleChangeUserName()
                }
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

    private fun initViewPager(){
        val fragmentList = listOf<Lazy<out DaggerFragment>>(latestChatFragmentProvider, contactFragmentProvider)
        val pagerAdapter = MainPagerAdapter(supportFragmentManager,fragmentList)
        view_pager.adapter = pagerAdapter

        tag_latest_msg.setColorFilter(ContextCompat.getColor(this,R.color.btn_bg))

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> tagMsgClick()
                    1 -> tagContactClick()
                }
            }

        })
        tag_latest_msg.setOnClickListener { view_pager.setCurrentItem(0,true) }
        tag_contact.setOnClickListener { view_pager.setCurrentItem(1,true) }
    }

    fun tagContactClick(){
        tag_contact.setColorFilter(ContextCompat.getColor(this,R.color.btn_bg))
        tag_latest_msg.setColorFilter(ContextCompat.getColor(this,R.color.gray))
    }

    fun tagMsgClick(){
        tag_contact.setColorFilter(ContextCompat.getColor(this,R.color.gray))
        tag_latest_msg.setColorFilter(ContextCompat.getColor(this,R.color.btn_bg))
    }

    @Inject fun addListener(context: Context){
        netChangeListener = object : NetworkReceiver.NetworkChangeListener{
            override fun networkChanged(status: Int) {
                if (status > NO_NETWORK){
                    if(MyApplication.mUser != null){
                        startService(Intent(context,NettyService::class.java))
                    }
                }else{
                    stopService(Intent(context,NettyService::class.java))
                }
            }
        }
        networkReceiver.addListener(netChangeListener)
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
        if (requestCode == ACTIVITY_SET_HEAD_REQ && resultCode == Activity.RESULT_OK) {
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
                                logi(TAG,"res width: ${resource.width}")
                                logi(TAG,"res height: ${resource.height}")
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

    private fun openAlbum() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, ACTIVITY_SET_HEAD_REQ)
    }

    override fun getLayoutId() = R.layout.activity_main

    @Inject override fun setViewModel(viewModel: MainViewModel) {
        mViewModel = viewModel
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            moveTaskToBack(true)
        }
    }

    override fun onDestroy() {
        logi(TAG,"onDestroy")
        stopService(Intent(this,NettyService::class.java))
        networkReceiver.removeListener(netChangeListener)
        super.onDestroy()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.add_friend -> handleAddFriend()
            R.id.change_password -> {
                startActivity(ChangePwdActivity::class.java)
            }
            R.id.log_out -> {
                MyApplication.mUser = null
                startActivity(Intent(this,LoginActivity::class.java))
                this.finish()
            }
            R.id.quit -> {
                super.onBackPressed()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun handleChangeUserName(){
        val view = View.inflate(this,R.layout.dialog_text_btn,null)
        with(view){
            dialog_text.setHint(R.string.user_name)
            dialog_btn.setText(R.string.change_user_name)
            dialog_btn.setOnClickListener {
                val userName = dialog_text.text.toString()

                if (!isNameLegal(userName)){
                    toast(R.string.user_name_hint)
                    return@setOnClickListener
                }
                mViewModel.updateUserName(userName)
            }
        }

        AlertDialog.Builder(this)
            .setView(view)
            .create()
            .show()
    }


    private fun handleAddFriend(){
        val view = View.inflate(this,R.layout.dialog_text_btn,null)
        with(view){
            dialog_text.setHint(R.string.name_or_phone)
            dialog_btn.setText(R.string.add_friend)
            dialog_btn.setOnClickListener {
                val nameOrPhone = dialog_text.text.toString()
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

        AlertDialog.Builder(this)
            .setView(view)
            .create()
            .show()
    }


}
