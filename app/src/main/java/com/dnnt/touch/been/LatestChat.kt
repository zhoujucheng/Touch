package com.dnnt.touch.been

import com.dnnt.touch.ui.main.message.MessageFragment
import java.util.*

/**
 * Created by dnnt on 18-3-15.
 */
data class LatestChat(var id: Long = MessageFragment.NONE, var headUrl: String = "", var nickname: String? = null, var time: Date = Date(), var latestMsg: String = "")