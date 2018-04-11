package com.dnnt.touch.ui.chat

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dnnt.touch.BR
import com.dnnt.touch.MyApplication
import com.dnnt.touch.R
import com.dnnt.touch.been.IMMsg
import com.dnnt.touch.been.User
import com.dnnt.touch.ui.base.BaseAdapter
import com.dnnt.touch.ui.main.contact.ItemEvenHandler

/**
 * Created by dnnt on 18-4-7.
 */
class ChatAdapter(var mUser: User) : BaseAdapter<IMMsg>() {

    companion object {
        const val TYPE_SEND = 1
        const val TYPE_RECEIVE = 2
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<IMMsg> {
        val binding = if (viewType == TYPE_SEND){
            DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context),R.layout.msg_send_item,parent,false)
        }else{
            DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context),R.layout.msg_receive_item,parent,false)
        }
        return ChatViewHolder(binding)
    }

    override fun getItemViewType(position: Int) =
        //如果发送者是本用户，则使用msg_send_item布局，如果发送者是与本用户对话的用户则使用msg_receive_item
       if (mList[position].from == MyApplication.mUser?.id){
           TYPE_SEND
       }else {
           TYPE_RECEIVE
       }

    override fun onBindViewHolder(holder: BaseViewHolder<IMMsg>, position: Int) {
        holder as ChatViewHolder
        if (mList[position].from == MyApplication.mUser?.id){
            holder.bind(mList[position],MyApplication.mUser as User)
        }else {
            holder.bind(mList[position],mUser)
        }

    }

    override fun getItemEvenHandler(): ItemEvenHandler<IMMsg>  =
        object : ItemEvenHandler<IMMsg>{
            override fun onItemClick(view: View, item: IMMsg) {}
        }

    override fun getLayoutId(): Int = -1

    class ChatViewHolder(binding: ViewDataBinding) : BaseViewHolder<IMMsg>(binding){
        fun bind(item: IMMsg, user: User) {
            mBinding.setVariable(BR.item,item)
            mBinding.setVariable(BR.user,user)
            mBinding.executePendingBindings()
        }
    }

}