package com.dnnt.touch.base

import android.databinding.BindingAdapter

import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dnnt.touch.util.BASE_URL


/**
 * Created by dnnt on 18-3-18.
 * 属性绑定，
 */
class ImageUrlBindings {
    companion object {
        @BindingAdapter("url")
        @JvmStatic
        fun setUrl(imageView: ImageView, url: String){
            var realUrl = url
            if(url.startsWith("/")){
                realUrl = BASE_URL + url.substring(1)
            }
            Glide.with(imageView)
                .load(realUrl)
//                .apply(RequestOptions.signatureOf())
                .into(imageView)
//            imageView.setImageURI()
        }
    }
}