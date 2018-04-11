package com.dnnt.touch.ui.main.message


import android.support.v7.widget.LinearLayoutManager

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

        with(recycler_view_message){
            layoutManager = LinearLayoutManager(this.context)
            adapter = mAdapter
        }
        EventBus.getDefault().register(this)
        //TODO init ui
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChatIdChange(latestChat: LatestChat){
        //进入ChatActivity时，将chatId设置为对话用户的id
        //退出ChatActivity时,将chatId设置为NONE
        chatId = latestChat.id
        if (chatId == NONE){
            EventBus.getDefault().removeAllStickyEvents()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMsgEvent(chatMsg: ChatProto.ChatMsg){
        val type = chatMsg.type
        when(type){
            TYPE_MSG -> {
                val imMsg = IMMsg.copyFromChatMsg(chatMsg)
                if (imMsg.from == chatId){
                    //将消息送到.ui.chat.ChatActivity
                    EventBus.getDefault().postSticky(imMsg)
                }else{
                    imMsg.async().save()
                }
                updateLatestChat(imMsg)
            }
            TYPE_ADD_FRIEND -> {
                val pair = getNameAndUrl(chatMsg.msg)
                val latestChat = LatestChat(chatMsg.from,pair.second,pair.first, Date(chatMsg.time),"", TYPE_ADD_FRIEND)
                latestChat.async().save()
                mAdapter.insertAtFirst(latestChat)
            }
            TYPE_FRIEND_AGREE -> {
                val pair = getNameAndUrl(chatMsg.msg)
                val latestChat = LatestChat(chatMsg.from,pair.second,
                    pair.first,Date(chatMsg.time),"", TYPE_MSG)
                latestChat.async().save()
                updateList(latestChat)
                updateUser(latestChat)
            }
            TYPE_OVERTIME -> {
                //TODO something wrong
                toast(R.string.add_friend_over_time,chatMsg.msg)
            }
            else -> {
                when{
                    type and TYPE_ACK != 0 -> {
                        if (type and TYPE_FRIEND_AGREE != 0){
                            val latestChat = (select from LatestChat::class where LatestChat_Table.id.eq(chatMsg.to)).querySingle()
                            if (latestChat != null){
                                latestChat.type = TYPE_MSG
                                latestChat.async().update()
                                updateList(latestChat)
                                updateUser(latestChat)
                            }
                        }else if(type and TYPE_MSG != 0){
                            val imMsg = IMMsg.copyFromChatMsg(chatMsg)
                            imMsg.from = imMsg.to // 注意此处
                            updateLatestChat(imMsg)
                        }
                    }
                }
            }
        }
    }

    private fun updateLatestChat(imMsg: IMMsg){
        val latestChat = (select from LatestChat::class where LatestChat_Table.id.eq(imMsg.from)).querySingle()
        if (latestChat != null) {
            latestChat.latestMsg = imMsg.msg
            latestChat.time = imMsg.time
            latestChat.async().update()
            updateList(latestChat)
        }
    }

    private fun updateUser(latestChat: LatestChat){
        val newUser = User(latestChat.id,latestChat.nickname,headUrl = latestChat.headUrl,nickname = latestChat.nickname)
        newUser.save()
        //TODO update ContactFragment ui
    }

    private fun updateList(latestChat: LatestChat){
        val k = mAdapter.mList.indexOfFirst { latestChat.id == it.id }
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
