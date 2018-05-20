package com.dnnt.touch.ui.main.message


import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.support.v7.widget.LinearLayoutManager
import com.dnnt.touch.MyApplication

import com.dnnt.touch.R
import com.dnnt.touch.been.*
import com.dnnt.touch.protobuf.ChatProto
import com.dnnt.touch.ui.base.BaseFragment
import com.dnnt.touch.ui.main.MainActivity
import com.dnnt.touch.ui.main.MainViewModel
import com.dnnt.touch.util.*
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.sql.language.Method
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.sql.language.Select
import kotlinx.android.synthetic.main.fragment_message.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject
import android.arch.lifecycle.Observer


class LatestChatFragment @Inject constructor() : BaseFragment<LatestChatViewModel>() {

    companion object {
        val TAG = "LatestChatFragment"
        val NONE = -1L
    }


    private val mAdapter = LatestChatAdapter()

    override fun init() {
        initUI()
        mViewModel.itemChangeEvent.observe(this,Observer {
            mAdapter.notifyItemChanged(it ?: 0)
        })
        mViewModel.initData()
        EventBus.getDefault().register(this)
    }

    private fun initUI(){
        with(recycler_view_message){
            layoutManager = LinearLayoutManager(this.context)
            adapter = mAdapter
        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChatIdChange(latestChat: LatestChat){
        //进入ChatActivity时，将chatId设置为对话用户的id
        //退出ChatActivity时,将chatId设置为NONE
        val chatId = latestChat.from
        mViewModel.chatId = chatId
        MyApplication.mUser?.friendId = chatId
        if (chatId == NONE){
            EventBus.getDefault().removeAllStickyEvents()
        }else{
            mViewModel.clearNum()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMsgEvent(chatMsg: ChatProto.ChatMsg){
        val type = chatMsg.type
        when(type){
            TYPE_MSG -> mViewModel.handleMsg(chatMsg)
            TYPE_ADD_FRIEND -> mViewModel.handleAddFriend(chatMsg)
            TYPE_FRIEND_AGREE -> mViewModel.handleFriendAgree(chatMsg)
            TYPE_OVERTIME -> toast(R.string.add_friend_over_time,chatMsg.msg)
            TYPE_HEAD_UPDATE -> mViewModel.handleHeadUpdate(chatMsg)
            else -> {
                if (type and TYPE_ACK != 0){
                    mViewModel.handleAck(type xor TYPE_ACK,chatMsg)
                }
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_message

    @Inject override fun setViewModule(viewModel: LatestChatViewModel) {
        mViewModel = viewModel
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

}
