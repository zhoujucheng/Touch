package com.dnnt.touch.base

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by dnnt on 18-4-7.
 */
abstract class RecyclerScrollListener : RecyclerView.OnScrollListener() {
    companion object {
        val TAG = "RecyclerScrollListener"
    }

    private var mLoading = true
    private var previousCount = 0

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val manager = recyclerView!!.layoutManager as LinearLayoutManager
        val firstVisible = manager.findFirstVisibleItemPosition()
        val itemCount = manager.itemCount
        val visibleCount = manager.childCount

        if (mLoading && itemCount >= previousCount){
            mLoading = false
            previousCount = itemCount
        } else if (!mLoading && visibleCount >= itemCount - firstVisible){
            if ((manager.reverseLayout && dy < 0) || (!manager.reverseLayout && dy > 0)){
                mLoading = true
                recyclerView.post {
                    loadMore()
                }
            }
        }
    }

    abstract fun loadMore()
}