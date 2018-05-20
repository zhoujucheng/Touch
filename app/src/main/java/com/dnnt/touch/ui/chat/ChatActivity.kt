package com.dnnt.touch.ui.chat

import com.dnnt.touch.R
import android.support.v7.widget.LinearLayoutManager
import com.dnnt.touch.MyApplication
import com.dnnt.touch.base.RecyclerScrollListener
import com.dnnt.touch.been.*
import com.dnnt.touch.ui.base.BaseActivity
import com.dnnt.touch.ui.main.message.LatestChatFragment
import com.dnnt.touch.util.*
import com.raizlabs.android.dbflow.kotlinextensions.*
import kotlinx.android.synthetic.main.activity_chat.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import javax.inject.Inject
import android.arch.lifecycle.Observer
import android.content.Intent

class ChatActivity : BaseActivity<ChatViewModel>() {

    private var chatUserId = 0L
    private lateinit var mAdapter: ChatAdapter


    override fun init() {
        EventBus.getDefault().register(this)
        chatUserId = intent.getLongExtra(CHAT_USER_ID,0)
        refresh()
    }

    private fun refresh(){
        //将消息发到.ui.main.message.LatestChatFragment,将LatestChatFragment的chatId设置为对话用户的id
        EventBus.getDefault().post(LatestChat(chatUserId))
        val user = mViewModel.initData(chatUserId)
        mAdapter = ChatAdapter(user)
        mViewModel.itemChangeEvent.observe(this,Observer {
            mAdapter.notifyItemChanged(it ?: 0)
        })
        initRecyclerView()
        setSendClickListener()
    }

    private fun initRecyclerView(){
        with(recycle_view_chat){
            layoutManager = LinearLayoutManager(this@ChatActivity,LinearLayoutManager.VERTICAL,true)
            adapter = mAdapter
            addOnScrollListener(object : RecyclerScrollListener() {
                override fun loadMore() {
                    mViewModel.loadMore(chatUserId)
                }
            })
        }
    }

    private fun setSendClickListener(){
        msg_send.setOnClickListener {
            val txt = edit_msg.text.toString()
            if (txt == ""){
                return@setOnClickListener
            }
            val msg = IMMsg(0,MyApplication.mUser?.id as Long,chatUserId,Date(),txt, TYPE_MSG)
            edit_msg.setText("")
            mViewModel.sendMsg(msg)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    fun onMsgEvent(imMsg: IMMsg){
        when(imMsg.type){
            TYPE_MSG ->{
                mViewModel.handleMsg(imMsg)
            }
            TYPE_ACK -> {
                mViewModel.handleAck(imMsg)
            }
            TYPE_SEND_FAIL -> {
                mViewModel.handleSendFail(imMsg)
            }
        }
    }

    override fun onDestroy() {
        //将消息发到.ui.main.message.LatestChatFragment,将LatestChatFragment的chatId设置为NONE
        EventBus.getDefault().post(LatestChat(LatestChatFragment.NONE))
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun getLayoutId() = R.layout.activity_chat

    @Inject
    override fun setViewModel(viewModel: ChatViewModel) {
        mViewModel = viewModel
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        logi("onNewIntent","invoke")
        chatUserId = intent?.getLongExtra(CHAT_USER_ID,0L) ?: 0L
        mViewModel.clear()
        refresh()
    }


}
