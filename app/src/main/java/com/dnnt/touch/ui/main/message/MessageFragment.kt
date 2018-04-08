package com.dnnt.touch.ui.main.message


import android.support.v7.widget.LinearLayoutManager

import com.dnnt.touch.R
import com.dnnt.touch.been.IMMsg
import com.dnnt.touch.been.LatestChat
import com.dnnt.touch.protobuf.ChatProto
import com.dnnt.touch.ui.base.BaseFragment
import com.dnnt.touch.ui.main.MainActivity
import com.dnnt.touch.ui.main.MainViewModel
import com.dnnt.touch.util.debugOnly
import com.dnnt.touch.util.logi
import com.raizlabs.android.dbflow.kotlinextensions.*
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

    //记录与该用户对话的用户的id,-1代表没有与之对话的用户，若不为-1则当前的可视页面应为ChatActivity
    private var chatId: Long = NONE

    private val mAdapter = LatestChatAdapter()

    override fun init() {

        with(recycler_view_message){
            layoutManager = LinearLayoutManager(this.context)
            adapter = mAdapter
        }
        EventBus.getDefault().register(this)
        debugOnly {
            val list = mutableListOf(LatestChat(1,"http://120.79.250.237:8080/test/fig1.png","aaaaaaa", Date(System.currentTimeMillis()), "asdfgh"),
                LatestChat(2,"http://120.79.250.237:8080/test/fig2.png","ddddddd", Date(System.currentTimeMillis()), "qwerty"))
            mAdapter.setList(list)
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChatIdChange(latestChat: LatestChat){
        chatId = latestChat.id
        //TODO if id == -1 update ui
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMsgEvent(chatMsg: ChatProto.ChatMsg){
        val imMsg = IMMsg.copyFromChatMsg(chatMsg)
        if (imMsg.from == chatId){
            imMsg.save()
            //将消息送到.ui.chat.ChatActivity
            EventBus.getDefault().post(imMsg)
        }else{
            imMsg.async().save()
            //TODO update ui
        }
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
