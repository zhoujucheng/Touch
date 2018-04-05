package com.dnnt.touch.ui.main.contact

import android.view.View

/**
 * Created by dnnt on 18-3-27.
 */
interface ItemEvenHandler<in T> {
    fun onItemClick(view: View,item: T)
}