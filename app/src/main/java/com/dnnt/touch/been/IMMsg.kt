package com.dnnt.touch.been

import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import android.view.View
import com.dnnt.touch.MyApplication
import com.dnnt.touch.base.AppDatabase
import com.dnnt.touch.protobuf.ChatProto
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import java.io.Serializable
import java.util.*

/**
 * Created by dnnt on 18-3-15.
 */
@Table(database = AppDatabase::class)
data class IMMsg(@PrimaryKey(autoincrement = true) var id: Long = 0,
                 @Column var from : Long = 0, @Column var to: Long = 0,
                 @Column var time: Date = Date(), @Column var msg: String = "",
                 var type:Int = 0,var seq:Int = 0,@Column var userId: Long = 0) : Serializable{

    init {
        userId = MyApplication.mUser?.id ?: 0
    }

    companion object {
        fun copyFromChatMsg(chatMsg: ChatProto.ChatMsg): IMMsg{
            return IMMsg(0,chatMsg.from,chatMsg.to,Date(chatMsg.time),chatMsg.msg,chatMsg.type,chatMsg.seq,MyApplication.mUser?.id ?: 0)
        }
    }
}