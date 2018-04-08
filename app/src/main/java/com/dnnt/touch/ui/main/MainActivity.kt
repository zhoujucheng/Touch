package com.dnnt.touch.ui.main

import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.dnnt.touch.MyApplication
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import com.dnnt.touch.R
import com.dnnt.touch.been.IMMsg
import com.dnnt.touch.been.User
import com.dnnt.touch.netty.NettyService
import com.dnnt.touch.protobuf.ChatProto
import com.dnnt.touch.ui.base.BaseActivity
import com.dnnt.touch.ui.main.contact.ContactFragment
import com.dnnt.touch.ui.main.message.MessageFragment
import com.dnnt.touch.util.BASE_URL
import com.dnnt.touch.util.debugOnly
import com.dnnt.touch.util.logi
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.sql.language.From
import dagger.Lazy
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.coroutines.experimental.android.UI
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

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val fragmentList = listOf<Lazy<out DaggerFragment>>(messageFragmentProvider, contactFragmentProvider)
        val pagerAdapter = MainPagerAdapter(supportFragmentManager,fragmentList)
        view_pager.adapter = pagerAdapter

//        EventBus.getDefault().register(this)
        debugOnly {
            (select from IMMsg::class).list.forEach {
                it.delete()
            }
            startService(Intent(this,NettyService::class.java))
            launch(UI){
                if (MyApplication.mUser != null){
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



//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        when (item.itemId) {
//            R.id.action_settings -> return true
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

//    override fun onDestroy() {
//        EventBus.getDefault().unregister(this)
//        super.onDestroy()
//    }
}
