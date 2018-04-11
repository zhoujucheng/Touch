package com.dnnt.touch.ui.chat

import com.dnnt.touch.MyApplication
import com.dnnt.touch.been.IMMsg
import com.dnnt.touch.been.IMMsg_Table
import com.dnnt.touch.been.LatestChat
import com.dnnt.touch.been.User
import com.dnnt.touch.di.ActivityScoped
import com.dnnt.touch.netty.MsgHandler
import com.dnnt.touch.ui.base.BaseViewModel
import com.raizlabs.android.dbflow.kotlinextensions.*
import java.util.*
import javax.inject.Inject

/**
 * Created by dnnt on 18-3-23.
 */
@ActivityScoped
class ChatViewModel @Inject() constructor() : BaseViewModel() {
    companion object {
        val TAG = "ChatViewModel"
    }

    private val limit = 15
    private var offset = 0
    lateinit var chatUser: User
    lateinit var mAdapter: ChatAdapter

    fun loadMore(chatUserId: Long){
        val userId = MyApplication.mUser?.id as Long
        val list = (select from IMMsg::class
                where ((IMMsg_Table.from.eq(userId)).and(IMMsg_Table.to.eq(chatUserId)))
                        .or(IMMsg_Table.from.eq(chatUserId).and(IMMsg_Table.to.eq(userId)))
                limit limit
                offset offset
                orderBy (IMMsg_Table.time.desc())).list
        mAdapter.insertAtLast(list)
        offset += list.size
    }

    fun handleMsg(imMsg: IMMsg){
        imMsg.async().save()
//        LatestChat(chatUser.id,chatUser.headUrl,chatUser.userName, imMsg.time,imMsg.msg).save()
        offset++
        mAdapter.insertAtFirst(imMsg)
    }

    fun handleAck(imMsg: IMMsg){
        imMsg.async().save()
//        LatestChat(chatUser.id,chatUser.headUrl,chatUser.userName, imMsg.time,imMsg.msg).save()
        offset++
    }

    fun sendMsg(imMsg: IMMsg){
        MsgHandler.sendMsg(imMsg)
        mAdapter.insertAtFirst(imMsg)
    }


}