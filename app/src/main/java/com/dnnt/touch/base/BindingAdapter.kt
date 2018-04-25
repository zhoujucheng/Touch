package com.dnnt.touch.base

import android.databinding.BindingAdapter
import android.databinding.BindingConversion

import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dnnt.touch.util.BASE_URL
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by dnnt on 18-3-18.
 *
 */
object BindingAdapter{

    private val dateFormat = SimpleDateFormat.getDateInstance() as SimpleDateFormat

    init {
        dateFormat.applyPattern("yyyy/MM/dd HH:mm")
    }

    //属性绑定
    @BindingAdapter("url")
    @JvmStatic
    fun setUrl(imageView: ImageView, url: String){
        var realUrl = url
        if(url.startsWith("/")){
            realUrl = BASE_URL + url.substring(1)
        }
        Glide.with(imageView)
            .load(realUrl)
            .into(imageView)

    }

    @BindingConversion
    @JvmStatic
    fun convertDate(date : Date): String = dateFormat.format(date)


}