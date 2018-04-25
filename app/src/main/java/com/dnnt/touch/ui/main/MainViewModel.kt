package com.dnnt.touch.ui.main

import android.arch.lifecycle.MutableLiveData
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.bumptech.glide.Glide
import com.dnnt.touch.MyApplication
import com.dnnt.touch.been.IMMsg
import com.dnnt.touch.been.User
import com.dnnt.touch.di.ActivityScoped
import com.dnnt.touch.network.NetService
import com.dnnt.touch.protobuf.ChatProto
import com.dnnt.touch.ui.base.BaseViewModel
import com.dnnt.touch.util.debugOnly
import com.dnnt.touch.util.logi
import com.raizlabs.android.dbflow.kotlinextensions.async
import io.reactivex.android.schedulers.AndroidSchedulers
import org.greenrobot.eventbus.EventBus
import com.dnnt.touch.util.subscribe
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileDescriptor
import java.io.FileOutputStream
import javax.inject.Inject

/**
 * Created by dnnt on 18-2-3.
 */
@ActivityScoped
class MainViewModel @Inject constructor(): BaseViewModel(){

    companion object {
        const val TAG = "MainViewModel"
    }

    val loadUsers = MutableLiveData<List<User>>()

    @Inject lateinit var netService: NetService
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
                    loadUsers.value = list
                }
            },{_,_ ->
            })
    }

    fun updateHead(resource: Bitmap, headCache: File){
        val tokenBody =  RequestBody.create(MediaType.parse("multipart/form-data"), MyApplication.mToken)
        resource.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(headCache))
        val fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), headCache)
        val filePart = MultipartBody.Part.createFormData("file", headCache.name, fileBody)
        netService.updateHead(tokenBody,filePart)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                MyApplication.mUser?.headUrl = it.obj ?: ""
            },{_,_ ->

            })
    }
}