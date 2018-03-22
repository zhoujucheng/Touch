package com.dnnt.touch.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.dnnt.touch.BR
import com.dnnt.touch.R
import com.dnnt.touch.been.LatestChat
import com.dnnt.touch.databinding.ContactItemBinding
import com.dnnt.touch.databinding.LatestChatItemBinding
import com.dnnt.touch.ui.base.BaseAdapter
import kotlinx.android.synthetic.main.latest_chat_item.view.*

/**
 * Created by dnnt on 18-3-15.
 */
class LatestChatAdapter : BaseAdapter<LatestChat>(){
    override fun getLayoutId() = R.layout.latest_chat_item

}