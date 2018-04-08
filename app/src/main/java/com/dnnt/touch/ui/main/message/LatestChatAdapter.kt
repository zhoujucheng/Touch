package com.dnnt.touch.ui.main.message

import android.content.Intent
import android.view.View
import com.dnnt.touch.R
import com.dnnt.touch.been.LatestChat
import com.dnnt.touch.ui.base.BaseAdapter
import com.dnnt.touch.ui.chat.ChatActivity
import com.dnnt.touch.ui.main.contact.ItemEvenHandler
import com.dnnt.touch.util.CHAT_USER_ID
import org.greenrobot.eventbus.EventBus

/**
 * Created by dnnt on 18-3-15.
 */
class LatestChatAdapter : BaseAdapter<LatestChat>(){

    override fun getItemEvenHandler(): ItemEvenHandler<LatestChat> {
        return object : ItemEvenHandler<LatestChat> {
            override fun onItemClick(view: View, item: LatestChat) {
                EventBus.getDefault().post(item)
                val intent = Intent(view.context,ChatActivity::class.java)
                intent.putExtra(CHAT_USER_ID,item.id)
                view.context.startActivity(intent)
            }

        }
    }

    override fun getLayoutId() = R.layout.latest_chat_item

}