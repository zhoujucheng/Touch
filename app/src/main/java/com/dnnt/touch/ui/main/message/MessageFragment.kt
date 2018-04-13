package com.dnnt.touch.ui.main.message


import android.support.v7.widget.LinearLayoutManager
import com.dnnt.touch.MyApplication

import com.dnnt.touch.R
import com.dnnt.touch.been.IMMsg
import com.dnnt.touch.been.LatestChat
import com.dnnt.touch.been.LatestChat_Table
import com.dnnt.touch.been.User
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
import java.util.*
import javax.inject.Inject


class MessageFragment @Inject constructor() : BaseFragment<MainViewModel>() {

    companion object {
        val TAG = "MessageFragment"
        val NONE = -1L
    }

    //记录与该用户对话的用户的id,NONE代表没有与之对话的用户，若不为NONE则当前的可视页面应为ChatActivity
    private var chatId: Long = NONE

    private val mAdapter = LatestChatAdapter()

    override fun init() {
        initUI()
        EventBus.getDefault().register(this)
    }

    private fun initUI(){
        with(recycler_view_message){
            layoutManager = LinearLayoutManager(this.context)
            adapter = mAdapter
        }
        val id = MyApplication.mUser?.id as Long
        val list = (select from LatestChat::class
                where LatestChat_Table.to.eq(id)
                orderBy LatestChat_Table.time.desc()).list
        mAdapter.setList(list)
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChatIdChange(latestChat: LatestChat){
        //进入ChatActivity时，将chatId设置为对话用户的id
        //退出ChatActivity时,将chatId设置为NONE
        chatId = latestChat.from
        if (chatId == NONE){
            EventBus.getDefault().removeAllStickyEvents()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMsgEvent(chatMsg: ChatProto.ChatMsg){
        val type = chatMsg.type
        when(type){
            TYPE_MSG -> handleMsg(chatMsg)
            TYPE_ADD_FRIEND -> handleAddFriend(chatMsg)
            TYPE_FRIEND_AGREE -> handleFriendAgree(chatMsg)
            TYPE_OVERTIME -> toast(R.string.add_friend_over_time,chatMsg.msg)
            else -> {
                if (type and TYPE_ACK != 0){
                    handleAck(type xor TYPE_ACK,chatMsg)
                }
            }
        }
    }

    private fun handleAck(type: Int,chatMsg: ChatProto.ChatMsg){
        when(type){
            TYPE_FRIEND_AGREE -> {
                val latestChat = (select from LatestChat::class
                        where LatestChat_Table.to.eq(chatMsg.from).and(LatestChat_Table.from.eq(chatMsg.to)))
                    .querySingle()
                if (latestChat != null){
                    latestChat.type = TYPE_MSG
                    latestChat.async().update()
                    updateList(latestChat)
                    updateUser(latestChat)
                }
            }
            TYPE_MSG -> {
                val imMsg = IMMsg.copyFromChatMsg(chatMsg)
                imMsg.from = imMsg.to // 注意此处
                updateLatestChat(imMsg)
            }
        }
    }

    private fun handleFriendAgree(chatMsg: ChatProto.ChatMsg){
        val pair = getNameAndUrl(chatMsg.msg)
        val latestChat = LatestChat(chatMsg.from,chatMsg.to,pair.second,
            pair.first,Date(chatMsg.time),"", TYPE_MSG)
        latestChat.async().save()
        updateList(latestChat)
        updateUser(latestChat)
    }

    private fun handleAddFriend(chatMsg: ChatProto.ChatMsg){
        val pair = getNameAndUrl(chatMsg.msg)
        val latestChat = LatestChat(chatMsg.from,chatMsg.to,pair.second,pair.first, Date(chatMsg.time),"", TYPE_ADD_FRIEND)
        latestChat.async().save()
        mAdapter.insertAtFirst(latestChat)
    }

    private fun handleMsg(chatMsg: ChatProto.ChatMsg){
        val imMsg = IMMsg.copyFromChatMsg(chatMsg)
        if (imMsg.from == chatId){
            //将消息送到.ui.chat.ChatActivity
            EventBus.getDefault().postSticky(imMsg)
        }else{
            imMsg.async().save()
        }
        updateLatestChat(imMsg)
    }

    private fun updateLatestChat(imMsg: IMMsg){
        val latestChat = (select from LatestChat::class
                where LatestChat_Table.from.eq(imMsg.from).and(LatestChat_Table.to.eq(MyApplication.mUser?.id as Long)))
            .querySingle()
        if (latestChat != null) {
            latestChat.latestMsg = imMsg.msg
            latestChat.time = imMsg.time
            latestChat.async().update()
            updateList(latestChat)
        }
    }

    private fun updateUser(latestChat: LatestChat){
        val newUser = User(latestChat.to,latestChat.from,latestChat.nickname,null,latestChat.headUrl,latestChat.nickname)
        //将消息送到.ui.main.ContactFragment
        EventBus.getDefault().post(newUser)
    }

    private fun updateList(latestChat: LatestChat){
        val k = mAdapter.mList.indexOfFirst { latestChat.from == it.from }
        if (k >= 0){
            mAdapter.mList.removeAt(k)
        }
        mAdapter.insertAtFirst(latestChat)
    }

    private fun getNameAndUrl(msg: String): Pair<String,String>{
        val i = msg.lastIndexOf(SPLIT_CHAR)
        val name = msg.substring(0,i)
        val headUrl = msg.substring(i+1)
        return Pair(name,headUrl)
    }

    override fun getLayoutId() = R.layout.fragment_message

    @Inject override fun setViewModule(viewModel: MainViewModel) {
        mViewModel = viewModel
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

}
