package com.dnnt.touch.base

import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.support.v7.widget.RecyclerView

import android.widget.ImageView
import android.widget.ListView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dnnt.touch.been.User
import com.dnnt.touch.ui.base.BaseAdapter
import com.dnnt.touch.ui.main.contact.ContactAdapter
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

    @BindingAdapter("items")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, items: List<Any>) {
        val adapter = recyclerView.adapter as? BaseAdapter<Any>
        adapter?.setList(items)
    }

    @BindingConversion
    @JvmStatic
    fun convertDate(date : Date): String = dateFormat.format(date)


}