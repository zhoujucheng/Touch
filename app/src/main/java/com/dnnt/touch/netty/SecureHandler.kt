package com.dnnt.touch.netty

import com.dnnt.touch.util.aesDecrypt
import com.dnnt.touch.util.aesEncrypt
import com.dnnt.touch.util.logi
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import java.security.Key
import javax.crypto.Cipher

/**
 * Created by dnnt on 18-4-16.
 */
class SecureHandler(val key: Key) : ChannelDuplexHandler(){
    companion object {
        val TAG = "SecureHandler"
    }

//    private val encryptCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
//    private val decryptCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
//
//    init {
//        encryptCipher.init(Cipher.ENCRYPT_MODE,key)
//        decryptCipher.init(Cipher.DECRYPT_MODE,key)
//    }

    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        if (msg is ByteBuf){
            val length = msg.readableBytes()
            val bytes = if (msg.hasArray()){
                logi(TAG,"arrayOffSet: ${msg.arrayOffset()}")
                logi(TAG,"readerIndex: ${msg.readerIndex()}")
                val array = msg.array().copyOfRange(msg.readerIndex(),length)
                array.forEachIndexed { i, b ->
                    logi(TAG,"a[$i] = $b")
                }
                aesDecrypt(key,array)
            }else{
                val array = ByteArray(length)
                msg.readBytes(array)
                aesDecrypt(key,array)
            }
            super.channelRead(ctx,Unpooled.copiedBuffer(bytes))
        }else{
            super.channelRead(ctx, msg)
        }
    }

    override fun write(ctx: ChannelHandlerContext?, msg: Any?, promise: ChannelPromise?) {
        if (msg is ByteBuf){
            val bytes = aesEncrypt(key,msg.array())
            super.write(ctx, Unpooled.copiedBuffer(bytes), promise)
        }else{
            super.write(ctx, msg, promise)
        }
    }
}