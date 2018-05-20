package com.dnnt.touch.ui.chat

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableArrayList
import com.dnnt.touch.MyApplication
import com.dnnt.touch.been.*
import com.dnnt.touch.di.ActivityScoped
import com.dnnt.touch.netty.MsgHandler
import com.dnnt.touch.ui.base.BaseViewModel
import com.raizlabs.android.dbflow.kotlinextensions.*
import javax.inject.Inject

/**
 * Created by dnnt on 18-3-23.
 */
@ActivityScoped
class ChatViewModel @Inject() constructor() : BaseViewModel() {
    companion object {
        val TAG = "ChatViewModel"
    }

    val items = ObservableArrayList<IMMsg>()
    val itemChangeEvent = MutableLiveData<Int>()

    private val limit = 15
    private var offset = 0


    fun loadMore(chatUserId: Long){
        val userId = MyApplication.mUser?.id as Long
        val list = (select from IMMsg::class
                where IMMsg_Table.userId.eq(userId).and(
                    ((IMMsg_Table.from.eq(userId)).and(IMMsg_Table.to.eq(chatUserId)))
                        .or(IMMsg_Table.from.eq(chatUserId).and(IMMsg_Table.to.eq(userId))))
                limit limit
                offset offset
                orderBy (IMMsg_Table.time.desc())).list

        items.addAll(list)

        offset += list.size
    }

    fun initData(chatUserId: Long): User{
        val id = MyApplication.mUser?.id as Long
        val user = (select from User::class
                where (User_Table.id.eq(id)).and(User_Table.friendId.eq(chatUserId)))
            .querySingle() as User
        loadMore(user.friendId)
        return user
    }

    fun handleMsg(imMsg: IMMsg){
        items.add(0,imMsg)
        imMsg.save()
        offset++
    }

    fun handleAck(imMsg: IMMsg){
        notifyIMMsgChange(imMsg)
        imMsg.save()
        offset++
    }

    fun handleSendFail(imMsg: IMMsg){
        notifyIMMsgChange(imMsg)
        imMsg.save()
        offset++
    }

    private fun notifyIMMsgChange(imMsg: IMMsg){
        items.forEachIndexed { i,item ->
            if (item.seq == imMsg.seq){
                itemChangeEvent.value = i
                return
            }
        }
    }

    fun sendMsg(imMsg: IMMsg){
        MsgHandler.sendMsg(imMsg)
        items.add(0,imMsg)
    }

    fun clear(){
        items.clear()
        offset = 0
    }
}