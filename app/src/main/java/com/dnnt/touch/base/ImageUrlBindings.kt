package com.dnnt.touch.base

import android.databinding.BindingAdapter

import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


/**
 * Created by dnnt on 18-3-18.
 */
class ImageUrlBindings {
    companion object {
        @BindingAdapter("url")
        @JvmStatic
        fun setUrl(imageView: ImageView, url: String){
            Glide.with(imageView)
                .load(url)
//                .apply(RequestOptions.signatureOf())
                .into(imageView)
        }
    }
}