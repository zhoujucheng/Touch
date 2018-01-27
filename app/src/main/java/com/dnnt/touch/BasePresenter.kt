package com.dnnt.touch

/**
 * Created by dnnt on 18-1-25.
 */
interface BasePresenter<T> {
    fun bind(view: T)
    fun unbind()
}