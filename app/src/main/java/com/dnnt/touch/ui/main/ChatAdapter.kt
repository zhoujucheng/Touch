package com.dnnt.touch.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.dnnt.touch.R
import com.dnnt.touch.been.LatestChat
import com.dnnt.touch.databinding.ContactItemBinding
import kotlinx.android.synthetic.main.item_latest_chat.view.*

/**
 * Created by dnnt on 18-3-15.
 */
class ChatAdapter(list: MutableList<LatestChat>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    var chatList: MutableList<LatestChat> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_latest_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount() = chatList.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.itemView.nickname.text = chatList[position].nickname
    }

    inner class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(){

        }
    }

}