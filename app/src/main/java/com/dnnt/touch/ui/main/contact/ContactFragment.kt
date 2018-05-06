package com.dnnt.touch.ui.main.contact

import android.arch.lifecycle.Observer
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import com.dnnt.touch.MyApplication

import com.dnnt.touch.R
import com.dnnt.touch.been.User
import com.dnnt.touch.been.User_Table
import com.dnnt.touch.ui.base.BaseFragment
import com.dnnt.touch.ui.main.MainViewModel
import com.dnnt.touch.util.TYPE_HEAD_UPDATE
import com.dnnt.touch.util.USER_NAME
import com.dnnt.touch.util.debugOnly
import com.raizlabs.android.dbflow.kotlinextensions.*
import kotlinx.android.synthetic.main.fragment_contact.*
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.coroutines.experimental.asReference
import javax.inject.Inject
import kotlin.coroutines.experimental.buildSequence


/**
 * A simple [Fragment] subclass.
 */
class ContactFragment @Inject constructor(): BaseFragment<MainViewModel>() {

    private val mAdapter = ContactAdapter()

    override fun init() {
        with(recycler_view_contact){
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        val id = MyApplication.mUser?.id as Long
        val list = (select from User::class where User_Table.id.eq(id)).list
        mAdapter.setList(list)
        if (list.size == 0){
            mViewModel.loadUsers.observe(this, Observer {
                mAdapter.setList(it?.toMutableList())
            })
            mViewModel.freshFriends(MyApplication.mToken)
        }
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun addOrUpdateUser(user: User){
        when(user.id){
            //-1代表新加的好友
            -1L -> {
                user.id = MyApplication.mUser?.id ?: 0L
                mAdapter.insertAtFirst(user)
                user.async().insert()
            }
            TYPE_HEAD_UPDATE.toLong() -> {
                mAdapter.mList.forEachIndexed { i, item ->
                    if (item.friendId == user.friendId){
                        item.headUrl = user.headUrl
                        item.async().update()
                        mAdapter.notifyItemChanged(i)
                        return@forEachIndexed
                    }
                }
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_contact

    @Inject override fun setViewModule(viewModel: MainViewModel) {
        mViewModel = viewModel
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

}
