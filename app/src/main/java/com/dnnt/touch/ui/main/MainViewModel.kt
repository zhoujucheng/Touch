package com.dnnt.touch.ui.main

import com.dnnt.touch.been.IMMsg
import com.dnnt.touch.di.ActivityScoped
import com.dnnt.touch.protobuf.ChatProto
import com.dnnt.touch.ui.base.BaseViewModel
import com.dnnt.touch.util.debugOnly
import com.raizlabs.android.dbflow.kotlinextensions.async
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

/**
 * Created by dnnt on 18-2-3.
 */
@ActivityScoped
class MainViewModel @Inject constructor(): BaseViewModel()