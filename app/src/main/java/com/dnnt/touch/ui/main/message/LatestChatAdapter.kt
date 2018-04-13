package com.dnnt.touch.ui.main.message

import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dnnt.touch.MyApplication
import com.dnnt.touch.R
import com.dnnt.touch.been.IMMsg
import com.dnnt.touch.been.LatestChat
import com.dnnt.touch.been.User
import com.dnnt.touch.netty.MsgHandler
import com.dnnt.touch.ui.base.BaseAdapter
import com.dnnt.touch.ui.chat.ChatActivity
import com.dnnt.touch.ui.main.contact.ItemEvenHandler
import com.dnnt.touch.util.*
import kotlinx.android.synthetic.main.add_friend_item.view.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by dnnt on 18-3-15.
 */
class LatestChatAdapter : BaseAdapter<LatestChat>(){

//    private val friendApply = mutableListOf<LatestChat>()

    companion object {
        const val ITEM = 1
        const val FRIEND_APPLY = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<LatestChat> =
        when(viewType){
            ITEM -> super.onCreateViewHolder(parent, viewType)
            else ->{
                val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context),R.layout.add_friend_item,parent,false)
                BaseViewHolder(binding)
            }
        }

    override fun getItemViewType(position: Int) =
        when{
            mList[position].type == TYPE_MSG -> ITEM
            else -> FRIEND_APPLY
        }

    override fun onBindViewHolder(holder: BaseViewHolder<LatestChat>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = mList[position]
        if (item.type == TYPE_ADD_FRIEND){
            holder.mBinding.root.tv_agree.setOnClickListener {
                val user = MyApplication.mUser as User
                val msg = user.userName + SPLIT_CHAR + user.headUrl
                MsgHandler.sendMsg(IMMsg(0,item.to,item.from,Date(),msg, TYPE_FRIEND_AGREE))
            }
        }

    }

    override fun getItemEvenHandler(): ItemEvenHandler<LatestChat> {
        return object : ItemEvenHandler<LatestChat> {
            override fun onItemClick(view: View, item: LatestChat) {
                //将消息发到.ui.main.message.MessageFragment,将MessageFragment的chatId设置为对话用户的id
                if (item.type == TYPE_MSG){
                    EventBus.getDefault().post(item)
                    val intent = Intent(view.context,ChatActivity::class.java)
                    intent.putExtra(CHAT_USER_ID,item.from)
                    view.context.startActivity(intent)
                }
            }

        }
    }

    override fun getLayoutId() = R.layout.latest_chat_item

}