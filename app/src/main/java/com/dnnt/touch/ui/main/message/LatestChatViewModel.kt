package com.dnnt.touch.ui.main.message

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Intent
import android.databinding.ObservableArrayList
import android.support.v4.app.NotificationCompat
import com.dnnt.touch.MyApplication
import com.dnnt.touch.R
import com.dnnt.touch.been.*
import com.dnnt.touch.protobuf.ChatProto
import com.dnnt.touch.ui.base.BaseViewModel
import com.dnnt.touch.ui.chat.ChatActivity
import com.dnnt.touch.ui.main.MainActivity
import com.dnnt.touch.util.*
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

    //记录与该用户对话的用户的id,NONE代表没有与之对话的用户，若不为NONE则当前的可视页面应为ChatActivity
    var chatId: Long = LatestChatFragment.NONE

    fun initData(){
        val id = MyApplication.mUser?.id as Long
        val list = (select from LatestChat::class
                where LatestChat_Table.to.eq(id)
                orderBy LatestChat_Table.time.desc()).list
        items.addAll(list)
    }

    fun clearNum(){
        val i = items.indexOfFirst { chatId == it.from }
        if (i >= 0){
            val item = items[i]
            if (item.num != 0){
                item.num = 0
                item.async().save()
                items[i] = item
            }
        }
    }

    fun handleMsg(chatMsg: ChatProto.ChatMsg){
        val imMsg = IMMsg.copyFromChatMsg(chatMsg)
        if (imMsg.from == chatId){
            //将消息送到.ui.chat.ChatActivity
            EventBus.getDefault().postSticky(imMsg)
        }else{
            imMsg.async().save()
        }
        val id = MyApplication.mUser?.id as Long
        val user = (select from User::class
                where User_Table.id.eq(id).and(User_Table.friendId.eq(imMsg.from)))
            .querySingle()
        if (user != null) {
            updateLatestChat(imMsg, user,true)
            val intent = Intent(MyApplication.mContext,ChatActivity::class.java)
            intent.putExtra(CHAT_USER_ID,imMsg.from)
            val intents = arrayOf(intent)
            val pendingIntent = PendingIntent.getActivities(MyApplication.mContext,1,intents,PendingIntent.FLAG_UPDATE_CURRENT)
            handleNotification(user.userName,chatMsg.msg,pendingIntent)
        }
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
        val id = MyApplication.mUser?.id as Long
        val user = (select from User::class
                where User_Table.id.eq(id).and(User_Table.friendId.eq(imMsg.from)))
            .querySingle()
        if (user != null) {
            updateLatestChat(imMsg,user)
        }
    }

    fun handleAddFriend(chatMsg: ChatProto.ChatMsg){
        val pair = getNameAndUrl(chatMsg.msg)
        val latestChat = LatestChat(chatMsg.from,chatMsg.to,pair.second,pair.first, Date(chatMsg.time),"", TYPE_ADD_FRIEND)
        latestChat.async().save()
        items.add(0,latestChat)
        val intents = arrayOf(Intent(MyApplication.mContext,MainActivity::class.java))
        val intent = PendingIntent.getActivities(MyApplication.mContext,1,intents,PendingIntent.FLAG_UPDATE_CURRENT)
        handleNotification(pair.first, getString(R.string.friend_apply),intent)
    }

    fun handleFriendAgree(chatMsg: ChatProto.ChatMsg){
        val pair = getNameAndUrl(chatMsg.msg)
        val latestChat = LatestChat(chatMsg.from,chatMsg.to,pair.second,
            pair.first,Date(chatMsg.time),"", TYPE_MSG
        )
        updateList(latestChat)
        updateUser(latestChat)
        val intents = arrayOf(Intent(MyApplication.mContext,MainActivity::class.java))
        val intent = PendingIntent.getActivities(MyApplication.mContext,1,intents,PendingIntent.FLAG_UPDATE_CURRENT)
        handleNotification(pair.first, getString(R.string.friend_agree),intent)
    }

    fun handleHeadUpdate(chatMsg: ChatProto.ChatMsg){
        val user = User(chatMsg.type.toLong(),chatMsg.from,headUrl = chatMsg.msg)
        //将消息送到.ui.main.ContactFragment
        EventBus.getDefault().post(user)
        items.forEachIndexed { i, item ->
            if (item.from == chatMsg.from){
                item.headUrl = chatMsg.msg
                item.async().save()
                itemChangeEvent.value = i
                return
            }
        }
    }

    fun handleUserNameUpdate(chatMsg: ChatProto.ChatMsg){
        val user = User(chatMsg.type.toLong(),chatMsg.from,userName = chatMsg.msg)
        //将消息送到.ui.main.ContactFragment
        EventBus.getDefault().post(user)
        items.forEachIndexed { i,item ->
            if (item.from == chatMsg.from){
                item.nickname = chatMsg.msg
                item.async().save()
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

    private fun updateList(latestChat: LatestChat,refreshNum: Boolean = false){
        val k = items.indexOfFirst { latestChat.from == it.from }
        if (k >= 0){
            val item = items.removeAt(k)
            if (refreshNum && latestChat.from != chatId){
                latestChat.num = item.num + 1
            }
        }
        latestChat.async().save()
        items.add(0,latestChat)
    }

    private fun updateUser(latestChat: LatestChat){
        val newUser = User(-1L,latestChat.from,latestChat.nickname,null,latestChat.headUrl,latestChat.nickname)
        //将消息送到.ui.main.ContactFragment
        EventBus.getDefault().post(newUser)
    }

    private fun updateLatestChat(imMsg: IMMsg,user: User,refreshNum: Boolean = false){
        //imMsg.from为对话用户的id，不论是发送消息还是接受消息
        val latestChat = LatestChat(imMsg.from,user.id,user.headUrl,user.userName,imMsg.time,imMsg.msg,imMsg.type)
        updateList(latestChat,refreshNum)
    }

    private fun handleACKForFriendAgree(chatMsg: ChatProto.ChatMsg){
        val latestChat = (select from LatestChat::class
                where LatestChat_Table.to.eq(chatMsg.from).and(LatestChat_Table.from.eq(chatMsg.to)))
            .querySingle()
        if (latestChat != null){
            latestChat.type = TYPE_MSG
            updateList(latestChat)
            updateUser(latestChat)
        }
    }

    private fun handleNotification(title: String,text: String,intent: PendingIntent){
        if(MyApplication.isOnBackGround()){
            val context = MyApplication.mContext
            val notification = NotificationCompat.Builder(context, CHANNEL_MSG_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(intent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .build()
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(NOTIFICATION_MSG_ID, notification)
        }
    }
}