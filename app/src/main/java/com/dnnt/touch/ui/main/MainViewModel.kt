package com.dnnt.touch.ui.main

import android.arch.lifecycle.MutableLiveData
import com.dnnt.touch.been.IMMsg
import com.dnnt.touch.been.User
import com.dnnt.touch.di.ActivityScoped
import com.dnnt.touch.network.NetService
import com.dnnt.touch.protobuf.ChatProto
import com.dnnt.touch.ui.base.BaseViewModel
import com.dnnt.touch.util.debugOnly
import com.raizlabs.android.dbflow.kotlinextensions.async
import io.reactivex.android.schedulers.AndroidSchedulers
import org.greenrobot.eventbus.EventBus
import com.dnnt.touch.util.subscribe
import javax.inject.Inject

/**
 * Created by dnnt on 18-2-3.
 */
@ActivityScoped
class MainViewModel @Inject constructor(): BaseViewModel(){

    val loadUsers = MutableLiveData<List<User>>()

    @Inject lateinit var netService: NetService
    fun freshFriends(token: String){
        netService.getFriends(token)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val list = it.obj
                if (list != null && list.isNotEmpty()){
                    loadUsers.value = list
                    list.forEach {
                        it.async().save()
                    }
                }
            },{_,_ ->
            })
    }
}