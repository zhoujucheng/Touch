package com.dnnt.touch.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dnnt.touch.R
import com.dnnt.touch.been.User
import com.dnnt.touch.databinding.ContactItemBinding
import com.dnnt.touch.ui.base.BaseAdapter

/**
 * Created by dnnt on 18-3-15.
 */
class ContactAdapter : BaseAdapter<User>(){
    override fun getLayoutId() = R.layout.contact_item

}