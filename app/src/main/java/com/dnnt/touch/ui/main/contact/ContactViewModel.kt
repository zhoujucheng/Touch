package com.dnnt.touch.ui.main.contact

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.dnnt.touch.MyApplication
import com.dnnt.touch.been.User
import com.dnnt.touch.been.User_Table
import com.dnnt.touch.network.NetService
import com.dnnt.touch.ui.base.BaseViewModel
import com.dnnt.touch.util.subscribe
import com.raizlabs.android.dbflow.kotlinextensions.*
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by dnnt on 18-3-27.
 */
class ContactViewModel @Inject constructor() : BaseViewModel() {
    companion object {
        val TAG = "ContactViewModel"
    }

    @Inject lateinit var netService: NetService


    val items = ObservableArrayList<User>()
    val itemChangeEvent = MutableLiveData<Int>()

    fun freshFriends(token: String){
        netService.getFriends(token)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val list = it.obj
                if (list != null && list.isNotEmpty()){
                    list.forEach {
                        it.friendId = it.id
                        it.id = MyApplication.mUser?.id ?: 0
                        it.async().save()
                    }
                    items.addAll(list)
                }
            },{_,_ ->
            })
    }

    fun initData(){
        val id = MyApplication.mUser?.id as Long
        val list = (select from User::class where User_Table.id.eq(id)).list
        if (list.size == 0){
            freshFriends(MyApplication.mToken)
        }else{
            items.addAll(list)
        }
    }

    fun addNewFriend(user: User){
        user.id = MyApplication.mUser?.id ?: 0L
        items.add(0,user)
        user.async().insert()
    }

    fun updateHead(user: User){
        items.forEachIndexed { i, item ->
            if (item.friendId == user.friendId){
                item.headUrl = user.headUrl
                item.async().update()
                itemChangeEvent.value = i
                return
            }
        }
    }




}