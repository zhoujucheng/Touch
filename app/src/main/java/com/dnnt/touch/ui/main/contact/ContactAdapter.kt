package com.dnnt.touch.ui.main.contact

import android.view.View
import com.dnnt.touch.R
import com.dnnt.touch.been.User
import com.dnnt.touch.ui.base.BaseAdapter

/**
 * Created by dnnt on 18-3-15.
 */
class ContactAdapter : BaseAdapter<User>(){

    override fun getItemEvenHandler(): ItemEvenHandler<User> {
        return object : ItemEvenHandler<User>{
            override fun onItemClick(view: View, item: User) {
                // TODO
            }

        }
    }

    override fun getLayoutId() = R.layout.contact_item

}