package com.dnnt.touch.ui.chat

import com.dnnt.touch.di.ActivityScoped
import com.dnnt.touch.ui.base.BaseViewModel
import javax.inject.Inject

/**
 * Created by dnnt on 18-3-23.
 */
@ActivityScoped
class ChatViewModel @Inject() constructor() : BaseViewModel() {
    companion object {
        val TAG = "ChatViewModel"
    }
}