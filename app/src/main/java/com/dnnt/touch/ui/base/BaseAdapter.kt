package com.dnnt.touch.ui.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dnnt.touch.BR

/**
 * Created by dnnt on 18-3-21.
 */
abstract class BaseAdapter<T>(list: MutableList<T> = mutableListOf()) : RecyclerView.Adapter<BaseAdapter.BaseViewHolder<T>>() {

    private var mList = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context),getLayoutId(),parent,false)
        return BaseViewHolder(binding)
    }

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(mList[position])
    }

    class BaseViewHolder<in T>(val mBinding: ViewDataBinding) : RecyclerView.ViewHolder(mBinding.root){
        fun bind(item: T){
            mBinding.setVariable(BR.item,item)
            mBinding.executePendingBindings()
        }
    }

    abstract fun getLayoutId(): Int

    fun setList(list: MutableList<T>?){
        mList = list ?: mutableListOf()
        notifyDataSetChanged()
    }

}