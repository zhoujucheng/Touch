package com.dnnt.touch.ui.main.message

import android.content.Intent
import android.view.View
import com.dnnt.touch.R
import com.dnnt.touch.been.LatestChat
import com.dnnt.touch.ui.base.BaseAdapter
import com.dnnt.touch.ui.chat.ChatActivity
import com.dnnt.touch.ui.main.contact.ItemEvenHandler

/**
 * Created by dnnt on 18-3-15.
 */
class LatestChatAdapter : BaseAdapter<LatestChat>(){

    override fun getItemEvenHandler(): ItemEvenHandler<LatestChat> {
        return object : ItemEvenHandler<LatestChat> {
            override fun onItemClick(view: View, item: LatestChat) {
                // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                view.context.startActivity(Intent(view.context,ChatActivity::class.java))
            }

        }
    }

    override fun getLayoutId() = R.layout.latest_chat_item

}