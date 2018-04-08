package com.dnnt.touch.ui.chat

import com.dnnt.touch.been.IMMsg
import com.dnnt.touch.been.IMMsg_Table
import com.dnnt.touch.di.ActivityScoped
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

    fun loadMore(userId: Long,limit: Int, offset: Int): MutableList<IMMsg> =
        (select from IMMsg::class
                where (IMMsg_Table.from.eq(userId)).or(IMMsg_Table.to.eq(userId))
                limit limit
                offset offset
                orderBy (IMMsg_Table.time.desc())).list

}