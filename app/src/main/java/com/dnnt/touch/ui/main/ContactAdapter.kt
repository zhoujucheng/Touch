package com.dnnt.touch.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dnnt.touch.R
import com.dnnt.touch.been.User
import com.dnnt.touch.databinding.ContactItemBinding
import kotlinx.android.synthetic.main.contact_item.view.*

/**
 * Created by dnnt on 18-3-15.
 */
class ContactAdapter(list: MutableList<User>) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>(){

    var contactList = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ContactItemBinding.inflate(inflater,parent,false)
        return ContactViewHolder(binding)
    }

    override fun getItemCount() = contactList.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contactList[position])
    }

    inner class ContactViewHolder(binding: ContactItemBinding) : RecyclerView.ViewHolder(binding.root){
        private val mBinding = binding
        fun bind(user: User){
            mBinding.user = user
        }
    }
}