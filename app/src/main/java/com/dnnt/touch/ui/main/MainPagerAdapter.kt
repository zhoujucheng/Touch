package com.dnnt.touch.ui.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.dnnt.touch.ui.base.BaseFragment
import dagger.Lazy
import dagger.android.support.DaggerFragment

/**
 * Created by dnnt on 18-3-9.
 */
class MainPagerAdapter(fm: FragmentManager, private val mList: List<Lazy<out DaggerFragment>>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = mList[position].get()

    override fun getCount() = mList.size
}