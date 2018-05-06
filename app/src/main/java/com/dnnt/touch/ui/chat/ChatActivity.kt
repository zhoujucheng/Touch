package com.dnnt.touch.ui.chat

import com.dnnt.touch.R
import android.support.v7.widget.LinearLayoutManager
import com.dnnt.touch.MyApplication
import com.dnnt.touch.base.RecyclerScrollListener
import com.dnnt.touch.been.*
import com.dnnt.touch.ui.base.BaseActivity
import com.dnnt.touch.ui.main.message.MessageFragment
import com.dnnt.touch.util.*
import com.raizlabs.android.dbflow.kotlinextensions.*
import kotlinx.android.synthetic.main.activity_chat.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import javax.inject.Inject

class ChatActivity : BaseActivity<ChatViewModel>() {

    private lateinit var user: User

    override fun init() {
        EventBus.getDefault().register(this)
        initData()
        initRecyclerView()
        setSendClickListener()

    }

    private fun initData(){
        val userId = intent.getLongExtra(CHAT_USER_ID,0)
        val id = MyApplication.mUser?.id as Long
        user = (select from User::class
                where (User_Table.id.eq(id)).and(User_Table.friendId.eq(userId)))
            .querySingle() as User
        mViewModel.mAdapter = ChatAdapter(user)
        mViewModel.loadMore(user.friendId)
    }

    private fun initRecyclerView(){
        with(recycle_view_chat){
            layoutManager = LinearLayoutManager(this@ChatActivity,LinearLayoutManager.VERTICAL,true)
            adapter = mViewModel.mAdapter
            addOnScrollListener(object : RecyclerScrollListener() {
                override fun loadMore() {
                    mViewModel.loadMore(user.friendId)
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
            val msg = IMMsg(0,MyApplication.mUser?.id as Long,user.friendId,Date(),txt, TYPE_MSG)
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
        //将消息发到.ui.main.message.MessageFragment,将MessageFragment的chatId设置为NONE
        EventBus.getDefault().post(LatestChat(MessageFragment.NONE))
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun getLayoutId() = R.layout.activity_chat

    @Inject
    override fun setViewModel(viewModel: ChatViewModel) {
        mViewModel = viewModel
    }



}
