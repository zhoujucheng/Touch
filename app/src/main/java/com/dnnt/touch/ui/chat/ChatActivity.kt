package com.dnnt.touch.ui.chat

import com.dnnt.touch.R
import android.os.Bundle
import com.dnnt.touch.ui.base.BaseActivity
import com.dnnt.touch.util.debugOnly
import javax.inject.Inject

class ChatActivity : BaseActivity<ChatViewModel>() {
    override fun init() {
        debugOnly {
//            mutableListOf<>()
//            muta
        }
    }

    override fun getLayoutId() = R.layout.activity_chat

    @Inject
    override fun setViewModel(viewModel: ChatViewModel) {
        mViewModel = viewModel
    }

}
