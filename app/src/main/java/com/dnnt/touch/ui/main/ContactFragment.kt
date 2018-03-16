package com.dnnt.touch.ui.main

import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager

import com.dnnt.touch.R
import com.dnnt.touch.been.User
import com.dnnt.touch.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_contact.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class ContactFragment @Inject constructor(): BaseFragment<MainViewModel>() {
    override fun init() {
        val list = mutableListOf(User(1,"abcd","18255132583","","","","nickname"),
                User(3,"dddd","18255131585","","","","aefefe"))
        recycler_view_contact.layoutManager = LinearLayoutManager(context)
        recycler_view_contact.adapter = ContactAdapter(list)
    }

    override fun getLayoutId() = R.layout.fragment_contact

    @Inject
    override fun setViewModule(viewModel: MainViewModel) {
        mViewModel = viewModel
    }

}// Required empty public constructor
