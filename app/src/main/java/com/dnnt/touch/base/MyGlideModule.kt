package com.dnnt.touch.base

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.dnnt.touch.MyApplication
import java.io.InputStream


/**
 * Created by dnnt on 18-3-18.
 */
@GlideModule
class MyGlideModule : AppGlideModule(){
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(MyApplication.mOkHttpClient))
    }
}