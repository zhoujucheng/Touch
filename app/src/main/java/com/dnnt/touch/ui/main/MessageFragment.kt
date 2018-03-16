package com.dnnt.touch.ui.main


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dnnt.touch.R
import com.dnnt.touch.been.LatestChat
import com.dnnt.touch.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_message.*
import java.util.*
import javax.inject.Inject


class MessageFragment @Inject constructor() : BaseFragment<MainViewModel>() {

    override fun init() {
        val list = mutableListOf(LatestChat("acd","aaaaaaa", Date(System.currentTimeMillis()), "love you"),
                LatestChat("adf","ddddddd", Date(System.currentTimeMillis()), "hate you"))
        recycler_view_message.layoutManager = LinearLayoutManager(this.context)
        recycler_view_message.adapter = ChatAdapter(list)
    }

    override fun getLayoutId() = R.layout.fragment_message

    @Inject
    override fun setViewModule(viewModel: MainViewModel) {
        mViewModel = viewModel
    }

}
