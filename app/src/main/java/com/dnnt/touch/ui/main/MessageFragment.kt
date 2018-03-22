package com.dnnt.touch.ui.main


import android.support.v7.widget.LinearLayoutManager

import com.dnnt.touch.R
import com.dnnt.touch.been.LatestChat
import com.dnnt.touch.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_message.*
import java.util.*
import javax.inject.Inject


class MessageFragment @Inject constructor() : BaseFragment<MainViewModel>() {

    private val mAdapter = LatestChatAdapter()

    override fun init() {
        val list = mutableListOf(LatestChat("http://120.79.250.237:8080/test/fig1.png","aaaaaaa", Date(System.currentTimeMillis()), "love you"),
                LatestChat("http://120.79.250.237:8080/test/fig2.png","ddddddd", Date(System.currentTimeMillis()), "hate you"))
        with(recycler_view_message){
            layoutManager = LinearLayoutManager(this.context)
            adapter = mAdapter
        }

        mAdapter.setList(list)
    }

    override fun getLayoutId() = R.layout.fragment_message

    @Inject override fun setViewModule(viewModel: MainViewModel) {
        mViewModel = viewModel
    }

}
