package com.dnnt.touch.ui.main.contact

import android.content.Intent
import android.view.View
import com.dnnt.touch.R
import com.dnnt.touch.been.LatestChat
import com.dnnt.touch.been.User
import com.dnnt.touch.ui.base.BaseAdapter
import com.dnnt.touch.ui.chat.ChatActivity
import com.dnnt.touch.util.CHAT_USER_ID
import org.greenrobot.eventbus.EventBus

/**
 * Created by dnnt on 18-3-15.
 */
class ContactAdapter : BaseAdapter<User>(){

    override fun getItemEvenHandler(): ItemEvenHandler<User> {
        return object : ItemEvenHandler<User>{
            override fun onItemClick(view: View, item: User) {
                //将消息发到.ui.main.message.MessageFragment,将MessageFragment的chatId设置为对话用户的id
                EventBus.getDefault().post(LatestChat(to = item.friendId))
                val intent = Intent(view.context, ChatActivity::class.java)
                intent.putExtra(CHAT_USER_ID,item.friendId)
                view.context.startActivity(intent)
            }

        }
    }

    override fun getLayoutId() = R.layout.contact_item

}