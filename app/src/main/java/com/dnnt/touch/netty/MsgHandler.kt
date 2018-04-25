package com.dnnt.touch.netty

import android.os.Handler
import android.os.HandlerThread
import com.dnnt.touch.MyApplication
import com.dnnt.touch.R
import com.dnnt.touch.been.IMMsg
import com.dnnt.touch.been.User
import com.dnnt.touch.protobuf.ChatProto
import com.dnnt.touch.receiver.NetworkReceiver
import com.dnnt.touch.util.*
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Created by dnnt on 18-4-6.
 */
class MsgHandler : ChannelDuplexHandler(){

    companion object {
        const val TAG = "MsgHandler"
        lateinit var ctx: ChannelHandlerContext
        private val map = hashMapOf<Int,IMMsg>()
        private var seq = 1 //消息序列
        fun sendMsg(msg: IMMsg){
            //TODO check network
            val i = seq++
            if (!NetworkReceiver.isNetUsable() || ctx.executor().isShutdown || ctx.executor().isTerminated){
                return
            }
            ctx.executor().execute{
                msg.seq = i
                val chatMsg = ChatProto.ChatMsg.newBuilder()
                    .setFrom(msg.from)
                    .setTo(msg.to)
                    .setMsg(msg.msg)
                    .setType(msg.type)
                    .setSeq(i)
                    .build()
                ctx.writeAndFlush(chatMsg)
                map[i] = msg
                logi(TAG, "seq = $i")
                map.forEach { t, u ->
                    logi(TAG,"send,map[$t]: type = ${u.type}, seq = ${u.seq}, msg = ${u.msg}")
                }
            }

            //定时器，超时移除
            ctx.executor().schedule({
                val temp = map[i]
                if (temp != null){
                    map.remove(i)
                    if (temp.type == TYPE_MSG){
                        temp.type = TYPE_OVERTIME
                        //将消息送到.ui.chat.ChatActivity
                        EventBus.getDefault().post(temp)
                    }else if (temp.type == TYPE_ADD_FRIEND){
                        //TODO 处理添加好友超时
                        EventBus.getDefault().post(ChatProto.ChatMsg.newBuilder()
                            .setType(TYPE_OVERTIME)
                            .setMsg(temp.msg)
                            .build())
                    }
                }
            },10000,TimeUnit.MILLISECONDS)
        }

        fun sendACK(from: Long,seq: Int, time: Long){
            if (!NetworkReceiver.isNetUsable() || ctx.executor().isShutdown || ctx.executor().isTerminated){
                return
            }
            ctx.executor().execute{
                ctx.writeAndFlush(ChatProto.ChatMsg.newBuilder()
                    .setType(TYPE_ACK)
                    .setFrom(from)
                    .setTime(time)
                    .setSeq(seq)
                    .build())
            }
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        logi(TAG,"channelActive")
        MsgHandler.ctx = ctx
        ctx.writeAndFlush(
            ChatProto.ChatMsg.newBuilder()
                .setFrom(MyApplication.mUser?.id as Long)
                .setMsg(MyApplication.mToken)
                .setType(TYPE_CONNECTED)
                .build())
    }

    override fun channelInactive(ctx: ChannelHandlerContext?) {
        super.channelInactive(ctx)
        logi(TAG,"channelInActive")
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
//        if (msg !is ChatProto.ChatMsg){
//            ctx.fireChannelRead(msg)
//            return
//        }
        msg as ChatProto.ChatMsg
        when (msg.type){
        //将消息送到.ui.main.message.MessageFragment
            TYPE_MSG, TYPE_ADD_FRIEND, TYPE_FRIEND_AGREE -> {
                EventBus.getDefault().post(msg)
                MsgHandler.sendACK(msg.from,msg.seq,msg.time)
            }
            TYPE_ACK -> {   //仅代表服务器收到了消息，对方有一定概率没收到消息
                map.forEach { t, u ->
                    logi(TAG,"ACK, map[$t]: type = ${u.type}, seq = ${u.seq}, msg = ${u.msg}")
                }
                val temp = map.remove(msg.seq)
                if (temp != null){
                    logi(TAG,"ACK: type = ${temp.type}, seq = ${temp.seq}, msg = ${temp.msg}")
                    when(temp.type){
                        TYPE_MSG -> {
                            temp.type = TYPE_ACK
                            //将消息送到.ui.chat.ChatActivity
                            EventBus.getDefault().post(temp)
                            val chatMsg = ChatProto.ChatMsg.newBuilder()
                                .setFrom(temp.from)
                                .setMsg(temp.msg)
                                .setTo(temp.to)
                                .setType(TYPE_ACK or  TYPE_MSG)
                                .setTime(temp.time.time)
                                .build()
                            //将消息送到.ui.main.message.MessageFragment
                            EventBus.getDefault().post(chatMsg)
                        }
                        TYPE_FRIEND_AGREE -> {
                            //将消息送到.ui.main.message.MessageFragment
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
            TYPE_HEAD_UPDATE -> EventBus.getDefault().post(msg)
        }
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