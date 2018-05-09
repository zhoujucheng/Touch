package com.dnnt.touch.ui.main

import android.graphics.Bitmap
import com.dnnt.touch.MyApplication
import com.dnnt.touch.R
import com.dnnt.touch.di.ActivityScoped
import com.dnnt.touch.network.NetService
import com.dnnt.touch.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import com.dnnt.touch.util.subscribe
import com.dnnt.touch.util.toast
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
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

    @Inject lateinit var netService: NetService

    fun updateHead(resource: Bitmap, headCache: File){
        val tokenBody =  RequestBody.create(MediaType.parse("multipart/form-data"), MyApplication.mToken)
        resource.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(headCache))
        val fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), headCache)
        val filePart = MultipartBody.Part.createFormData("file", headCache.name, fileBody)
        netService.updateHead(tokenBody,filePart)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                MyApplication.mUser?.headUrl = it.obj ?: ""
                toast(R.string.update_head_success)
            },{msg,_ ->
                toast(msg)
            })
    }
}