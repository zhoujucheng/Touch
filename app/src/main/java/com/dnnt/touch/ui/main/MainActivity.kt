package com.dnnt.touch.ui.main

import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.dnnt.touch.MyApplication
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import com.dnnt.touch.R
import com.dnnt.touch.been.IMMsg
import com.dnnt.touch.been.LatestChat
import com.dnnt.touch.been.User
import com.dnnt.touch.netty.MsgHandler
import com.dnnt.touch.netty.NettyService
import com.dnnt.touch.protobuf.ChatProto
import com.dnnt.touch.ui.base.BaseActivity
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
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel>(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        val TAG = "MainActivity"
    }

    @Inject lateinit var messageFragmentProvider: Lazy<MessageFragment>
    @Inject lateinit var contactFragmentProvider: Lazy<ContactFragment>

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

        debugOnly {
            (select from IMMsg::class).list.forEach {
                it.delete()
            }
            (select from LatestChat::class).list.forEach {
                it.delete()
            }
            (select from User::class).list.forEach {
                it.delete()
            }
            launch(UI){
                if (MyApplication.mUser != null){
                    //TODO Have a better solutions?
                    while (user_head == null)   delay(100)
                    Glide.with(this@MainActivity).load(BASE_URL + MyApplication.mUser?.headUrl).into(user_head)
                }
            }
        }
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.add_friend -> {
                val view = View.inflate(this,R.layout.dialog_add_friend,null)
                AlertDialog.Builder(this)
                    .setView(view)
                    .create()
                    .show()
                with(view){
                    btn_add_friend.setOnClickListener {
                        val nameOrPhone = name_or_phone.text.toString()
                        val user = MyApplication.mUser as User
                        val id = user.id
                        if(nameOrPhone == user.userName || nameOrPhone.equals(user.phone)){
                            toast(R.string.can_not_add_yourself)
                            return@setOnClickListener
                        }
                        when {
                            isNameLegal(nameOrPhone) -> {
                                MsgHandler.sendMsg(IMMsg(from = id,msg = nameOrPhone,type = TYPE_ADD_FRIEND))
                            }
                            nameOrPhone.matches(Regex("\\d{11}")) -> {
                                MsgHandler.sendMsg(IMMsg(from = id,msg = nameOrPhone,type = TYPE_ADD_FRIEND))
                            }
                            else -> toast(R.string.user_not_exist)
                        }
                    }
                }
            }

//            R.id.nav_camera -> {
//                // Handle the camera action
//            }
//            R.id.nav_gallery -> {
//
//            }
//            R.id.nav_slideshow -> {
//
//            }
//            R.id.nav_manage -> {
//
//            }
//            R.id.nav_share -> {
//
//            }
//            R.id.nav_send -> {
//
//            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
