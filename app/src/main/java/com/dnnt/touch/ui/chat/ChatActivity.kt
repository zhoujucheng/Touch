package com.dnnt.touch.ui.chat

import com.dnnt.touch.R
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.dnnt.touch.MyApplication
import com.dnnt.touch.base.RecyclerScrollListener
import com.dnnt.touch.been.*
import com.dnnt.touch.netty.NettyHandler
import com.dnnt.touch.protobuf.ChatProto
import com.dnnt.touch.ui.base.BaseActivity
import com.dnnt.touch.ui.main.message.MessageFragment
import com.dnnt.touch.util.*
import com.raizlabs.android.dbflow.kotlinextensions.*
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import javax.inject.Inject

class ChatActivity : BaseActivity<ChatViewModel>() {

    private lateinit var user: User
    private val limit = 10
    private var offset = 0
    lateinit var mAdapter: ChatAdapter

    override fun init() {


        debugOnly {
            User(1,"dtjc","18255132583","","","/user/head/default.png","dtjc").save()
            User(2,"dnnt","18255132585","","","/user/head/default.png","dnnt").save()
            launch(CommonPool) {
                delay(10000)
                var i = 0
                repeat(100){
                    delay(500)
                    launch(UI) {
                        edit_msg.setText("$i,send from ${user.id}")
                        msg_send.performClick()
                    }.join()
                    i++
                }
            }
        }

        EventBus.getDefault().register(this)
        initData()
        initRecyclerView()
        setSendClickLisener()
    }

    private fun initData(){
        val userId = intent.getLongExtra(CHAT_USER_ID,0)
        user = (select from User::class where (User_Table.id.eq(userId))).querySingle() as User
        mAdapter = ChatAdapter(user)
        val list = mViewModel.loadMore(user.id,limit,offset)
        mAdapter.insertAtLast(list)
        offset += list.size
    }

    private fun initRecyclerView(){
        with(recycle_view_chat){
            layoutManager = LinearLayoutManager(this@ChatActivity,LinearLayoutManager.VERTICAL,true)
            adapter = mAdapter
            addOnScrollListener(object : RecyclerScrollListener() {
                override fun loadMore() {
                    val list = mViewModel.loadMore(user.id,limit,offset)
                    mAdapter.insertAtLast(list)
                    offset += list.size
                }
            })
        }
    }

    private fun setSendClickLisener(){
        //TODO 不保证100%能发送成功
        msg_send.setOnClickListener {
            val txt = edit_msg.text.toString()
            if (txt == ""){
                return@setOnClickListener
            }
            val msg = IMMsg(0,MyApplication.mUser?.id as Long,user.id,Date(),txt)
            edit_msg.setText("")
            NettyHandler.sendMsg(msg)
            mAdapter.insertAtFirst(msg)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMsgEvent(imMsg: IMMsg){
        when(imMsg.type){
            TYPE_MSG ->{
                offset++
                mAdapter.insertAtFirst(imMsg)
            }
            TYPE_ACK -> {
                imMsg.save()
                offset++
                //TODO update ui
            }
            TYPE_OVER_TIME -> {
                //TODO update ui
            }
        }

    }

    override fun onDestroy() {
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
