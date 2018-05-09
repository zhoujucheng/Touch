package com.dnnt.touch.ui.main.message

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableArrayList
import com.dnnt.touch.MyApplication
import com.dnnt.touch.been.*
import com.dnnt.touch.protobuf.ChatProto
import com.dnnt.touch.ui.base.BaseViewModel
import com.dnnt.touch.util.SPLIT_CHAR
import com.dnnt.touch.util.TYPE_ADD_FRIEND
import com.dnnt.touch.util.TYPE_FRIEND_AGREE
import com.dnnt.touch.util.TYPE_MSG
import com.raizlabs.android.dbflow.kotlinextensions.*
import org.greenrobot.eventbus.EventBus
import java.util.*
import javax.inject.Inject

/**
 * Created by dnnt on 18-5-9.
 */
class LatestChatViewModel @Inject constructor() : BaseViewModel(){

    val items = ObservableArrayList<LatestChat>()
    val itemChangeEvent = MutableLiveData<Int>()

    fun initData(){
        val id = MyApplication.mUser?.id as Long
        val list = (select from LatestChat::class
                where LatestChat_Table.to.eq(id)
                orderBy LatestChat_Table.time.desc()).list
        items.addAll(list)
    }

    fun handleMsg(chatMsg: ChatProto.ChatMsg,chatId: Long){
        val imMsg = IMMsg.copyFromChatMsg(chatMsg)
        if (imMsg.from == chatId){
            //将消息送到.ui.chat.ChatActivity
            EventBus.getDefault().postSticky(imMsg)
        }else{
            imMsg.async().save()
        }
        updateLatestChat(imMsg)
    }

    fun handleAck(type: Int,chatMsg: ChatProto.ChatMsg){
        when(type){
            TYPE_FRIEND_AGREE -> handleACKForFriendAgree(chatMsg)
            TYPE_MSG -> handleACKForMsg(chatMsg)
        }
    }

    private fun handleACKForMsg(chatMsg: ChatProto.ChatMsg){
        val imMsg = IMMsg.copyFromChatMsg(chatMsg)
        imMsg.from = imMsg.to // 注意此处
        imMsg.type = TYPE_MSG
        updateLatestChat(imMsg)
    }

    fun handleAddFriend(chatMsg: ChatProto.ChatMsg){
        val pair = getNameAndUrl(chatMsg.msg)
        val latestChat = LatestChat(chatMsg.from,chatMsg.to,pair.second,pair.first, Date(chatMsg.time),"", TYPE_ADD_FRIEND)
        latestChat.async().save()
        items.add(0,latestChat)
    }

    fun handleFriendAgree(chatMsg: ChatProto.ChatMsg){
        val pair = getNameAndUrl(chatMsg.msg)
        val latestChat = LatestChat(chatMsg.from,chatMsg.to,pair.second,
            pair.first,Date(chatMsg.time),"", TYPE_MSG
        )
        latestChat.async().save()
        updateList(latestChat)
        updateUser(latestChat)
    }

    fun handleHeadUpdate(chatMsg: ChatProto.ChatMsg){
        val user = User(chatMsg.type.toLong(),chatMsg.from,headUrl = chatMsg.msg)
        //将消息送到.ui.main.ContactFragment
        EventBus.getDefault().post(user)
        items.forEachIndexed { i, item ->
            if (item.from == chatMsg.from){
                item.async().save()
                item.headUrl = chatMsg.msg
                itemChangeEvent.value = i
                return
            }
        }

    }

    private fun getNameAndUrl(msg: String): Pair<String,String>{
        val i = msg.lastIndexOf(SPLIT_CHAR)
        val name = msg.substring(0,i)
        val headUrl = msg.substring(i+1)
        return Pair(name,headUrl)
    }

    private fun updateList(latestChat: LatestChat){
        val k = items.indexOfFirst { latestChat.from == it.from }
        if (k >= 0){
            items.removeAt(k)
        }
        items.add(0,latestChat)
    }

    private fun updateUser(latestChat: LatestChat){
        val newUser = User(-1L,latestChat.from,latestChat.nickname,null,latestChat.headUrl,latestChat.nickname)
        //将消息送到.ui.main.ContactFragment
        EventBus.getDefault().post(newUser)
    }

    private fun updateLatestChat(imMsg: IMMsg){
        //imMsg.from为对话用户的id，不论是发送消息还是接受消息
        val id = MyApplication.mUser?.id as Long
        val user = (select from User::class
                where User_Table.id.eq(id).and(User_Table.friendId.eq(imMsg.from)))
            .querySingle()
        if (user != null){
            val latestChat = LatestChat(imMsg.from,id,user.headUrl,user.userName,imMsg.time,imMsg.msg,imMsg.type)
            latestChat.async().save()
            updateList(latestChat)
        }
    }

    private fun handleACKForFriendAgree(chatMsg: ChatProto.ChatMsg){
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
}