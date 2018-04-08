package com.dnnt.touch.netty

import com.dnnt.touch.MyApplication
import com.dnnt.touch.been.IMMsg
import com.dnnt.touch.protobuf.ChatProto
import com.dnnt.touch.util.*
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit

/**
 * Created by dnnt on 18-4-6.
 */
class NettyHandler : ChannelDuplexHandler(){


    companion object {

        lateinit var ctx: ChannelHandlerContext
        private val map = hashMapOf<Int,ChatProto.ChatMsg>()
        private var seq = 1

        fun sendMsg(msg: IMMsg){
            msg.seq = seq
            ctx.executor().submit{
                val chatMsg = ChatProto.ChatMsg.newBuilder()
                    .setFrom(msg.from)
                    .setTo(msg.to)
                    .setMsg(msg.msg)
                    .setType(TYPE_MSG)
                    .setSeq(seq)
                    .build()
                ctx.writeAndFlush(chatMsg)
                map[seq] = chatMsg
            }

            //定时器，超时移除
            ctx.executor().schedule({
                val i = seq++
                val temp = map[i]
                if (temp != null){
                    map.remove(i)
                    //将消息送到.ui.chat.ChatActivity
                    EventBus.getDefault().post(IMMsg(to = temp.to,type = TYPE_OVER_TIME,seq = i))
                }
            },10000,TimeUnit.MILLISECONDS)
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        NettyHandler.ctx = ctx
        ctx.writeAndFlush(
            ChatProto.ChatMsg.newBuilder()
                .setFrom(MyApplication.mUser?.id as Long)
                .setType(TYPE_CONNECTED)
                .build())
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        //将消息送到.ui.main.message.MessageFragment
//        EventBus.getDefault().post(msg)
        msg as ChatProto.ChatMsg
        when (msg.type){
            TYPE_MSG -> EventBus.getDefault().post(msg)
            TYPE_ACK -> {
                val temp = map.remove(msg.seq)
                //将消息送到.ui.chat.ChatActivity
                EventBus.getDefault().post(IMMsg(to = temp?.to ?: 0,type = TYPE_ACK,seq = msg.seq))
            }
        }

    }
}