package com.dnnt.touch.ui.main.contact

import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import com.dnnt.touch.MyApplication

import com.dnnt.touch.R
import com.dnnt.touch.been.User
import com.dnnt.touch.been.User_Table
import com.dnnt.touch.ui.base.BaseFragment
import com.dnnt.touch.ui.main.MainViewModel
import com.dnnt.touch.util.USER_NAME
import com.dnnt.touch.util.debugOnly
import com.raizlabs.android.dbflow.kotlinextensions.*
import kotlinx.android.synthetic.main.fragment_contact.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject
import kotlin.coroutines.experimental.buildSequence


/**
 * A simple [Fragment] subclass.
 */
class ContactFragment @Inject constructor(): BaseFragment<MainViewModel>() {

    private val mAdapter = ContactAdapter()

    override fun init() {
        with(recycler_view_contact){
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        val id = MyApplication.mUser?.id as Long
        val list = (select from User::class where User_Table.id.eq(id)).list
        mAdapter.setList(list)
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun addNewContact(user: User){
        user.async().save()
        mAdapter.insertAtFirst(user)
    }

    override fun getLayoutId() = R.layout.fragment_contact

    @Inject override fun setViewModule(viewModel: MainViewModel) {
        mViewModel = viewModel
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

}
