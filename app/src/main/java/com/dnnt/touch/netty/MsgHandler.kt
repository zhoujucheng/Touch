package com.dnnt.touch.netty

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.dnnt.touch.MyApplication
import com.dnnt.touch.R
import com.dnnt.touch.been.IMMsg
import com.dnnt.touch.protobuf.ChatProto
import com.dnnt.touch.receiver.NetworkReceiver
import com.dnnt.touch.ui.login.LoginActivity
import com.dnnt.touch.util.*
import com.raizlabs.android.dbflow.kotlinextensions.async
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit

/**
 * Created by dnnt on 18-4-6.
 */
class MsgHandler : ChannelDuplexHandler(){

    companion object {
        const val TAG = "MsgHandler"
        private var ctx: ChannelHandlerContext? = null
        private val map = hashMapOf<Int,IMMsg>()
        private var seq = 1 //消息序列
        fun sendMsg(msg: IMMsg){
            val i = seq++
            msg.seq = i
            if (!NetworkReceiver.isNetUsable() || ctx == null){
                handleFailure(msg)
                toast(R.string.network_not_available)
                return
            }
            ctx?.executor()?.execute{
                val chatMsg = ChatProto.ChatMsg.newBuilder()
                    .setFrom(msg.from)
                    .setTo(msg.to)
                    .setMsg(msg.msg)
                    .setType(msg.type)
                    .setSeq(i)
                    .build()
                ctx?.writeAndFlush(chatMsg)
                map[i] = msg
                logi(TAG, "seq = $i")
                for((t,u) in map){
                    logi(TAG,"send,map[$t]: type = ${u.type}, seq = ${u.seq}, msg = ${u.msg}")
                }
            }

            //定时器，超时移除
            ctx?.executor()?.schedule({
                val temp = map[i]
                if (temp != null){
                    map.remove(i)
                    handleFailure(temp)
                }
            },10000,TimeUnit.MILLISECONDS)
        }

        private fun handleFailure(temp: IMMsg){
            if (temp.type == TYPE_MSG){
                temp.type = TYPE_SEND_FAIL
                handleIMMsg(temp)
            }else if (temp.type == TYPE_ADD_FRIEND){
                //将消息送到.ui.main.message.LatestChatFragment
                EventBus.getDefault().post(ChatProto.ChatMsg.newBuilder()
                    .setType(TYPE_OVERTIME)
                    .setMsg(temp.msg)
                    .build())
            }
        }

        fun sendACK(from: Long,seq: Int, to: Long){
            if (!NetworkReceiver.isNetUsable()){
                return
            }
            ctx?.executor()?.execute{
                ctx?.writeAndFlush(ChatProto.ChatMsg.newBuilder()
                    .setType(TYPE_ACK)
                    .setFrom(from)
                    .setTo(to)
                    .setSeq(seq)
                    .build())
            }
        }

        fun handleIMMsg(temp: IMMsg){
            if (temp.to == MyApplication.mUser?.friendId){
                //将消息送到.ui.chat.ChatActivity
                EventBus.getDefault().post(temp)
            }else{
                temp.async().save()
            }
        }

        fun close(){
            ctx?.let {
                if (!(it.executor().isShutdown || it.executor().isTerminated)){
                    it.close()
                    ctx = null
                }
            }

        }
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        logi(TAG,"channelActive")
        MsgHandler.ctx = ctx
        MyApplication.mUser?.let {
            ctx.writeAndFlush(
                ChatProto.ChatMsg.newBuilder()
                    .setFrom(it.id)
                    .setMsg(MyApplication.mToken)
                    .setType(TYPE_CONNECTED)
                    .build())
        }

    }

    override fun channelInactive(ctx: ChannelHandlerContext?) {
        super.channelInactive(ctx)
        logi(TAG,"channelInActive")
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        msg as ChatProto.ChatMsg
        when (msg.type){
            TYPE_MSG, TYPE_ADD_FRIEND, TYPE_FRIEND_AGREE -> {
                //将消息送到.ui.main.message.LatestChatFragment
                EventBus.getDefault().post(msg)
                MsgHandler.sendACK(msg.from,msg.seq,msg.to)

            }
            TYPE_ACK -> {   //仅代表服务器收到了消息，对方有一定概率没收到消息
                for ((t,u) in map){
                    logi(TAG,"ACK, map[$t]: type = ${u.type}, seq = ${u.seq}, msg = ${u.msg}")
                }
                val temp = map.remove(msg.seq)
                if (temp != null){
                    logi(TAG,"ACK: type = ${temp.type}, seq = ${temp.seq}, msg = ${temp.msg}")
                    when(temp.type){
                        TYPE_MSG -> {
                            temp.type = TYPE_ACK
                            handleIMMsg(temp)
                            val chatMsg = ChatProto.ChatMsg.newBuilder()
                                .setFrom(temp.from)
                                .setMsg(temp.msg)
                                .setTo(temp.to)
                                .setType(TYPE_ACK or  TYPE_MSG)
                                .setTime(temp.time.time)
                                .build()
                            //将消息送到.ui.main.message.LatestChatFragment
                            EventBus.getDefault().post(chatMsg)
                        }
                        TYPE_FRIEND_AGREE -> {
                            //将消息送到.ui.main.message.LatestChatFragment
                            val chatMsg = msg.toBuilder()
                                .setFrom(temp.from)
                                .setTo(temp.to)
                                .setType(TYPE_ACK or TYPE_FRIEND_AGREE)
                                .build()
                            EventBus.getDefault().post(chatMsg)
                        }
                    //DO NOTHING TYPE_ADD_FRIEND
                    }
                }else{
                    logi(TAG,"ACK: temp is null")
                }
            }
            TYPE_USER_NOT_EXIST -> {
                val temp = map.remove(msg.seq)
                logi(TAG,temp?.msg ?: "null")
                launch(UI) { toast(R.string.user_not_exist,temp?.msg ?: "") }
            }
            TYPE_USER_ALREADY_ADD -> {
                val temp = map.remove(msg.seq)
                launch(UI) { toast(R.string.friend_already_add,temp?.msg ?: "") }
            }
            //将消息送到.ui.main.message.LatestChatFragment
            TYPE_HEAD_UPDATE, TYPE_UPDATE_USER_NAME -> EventBus.getDefault().post(msg)
            TYPE_SEND_FAIL ->{
                val temp = map.remove(msg.seq)
                if (temp != null){
                    temp.type = TYPE_SEND_FAIL
                    handleIMMsg(temp)
                }
            }
            TYPE_TOKEN_WRONG -> {
                logi(TAG,"wrong token")
                handleWrongToken()
            }
            TYPE_OTHER_LOGIN -> {
                NettyService.terminate()
                val context = MyApplication.mContext
                context.startActivity(Intent(context,LoginActivity::class.java))
                finishAllActivity()
            }
        }
    }

    private fun handleWrongToken(){
        NettyService.terminate()
        removeSensitiveInfo(MyApplication.mContext)
        val context = MyApplication.mContext
        toastLong(R.string.other_login_tip)
        context.startActivity(Intent(context,LoginActivity::class.java))
        finishAllActivity()
    }


    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
        if (evt is IdleStateEvent){
            if (evt.state() == IdleState.WRITER_IDLE){
                ctx?.writeAndFlush(ChatProto.ChatMsg.newBuilder()
                    .setType(TYPE_HEARTBEAT)
                    .build())
            }
        }else{
            super.userEventTriggered(ctx, evt)
        }
    }

}