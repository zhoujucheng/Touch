package com.dnnt.touch.ui.main

import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager

import com.dnnt.touch.R
import com.dnnt.touch.been.User
import com.dnnt.touch.ui.base.BaseFragment
import com.dnnt.touch.util.debugOnly
import kotlinx.android.synthetic.main.fragment_contact.*
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
        debugOnly {
            val users = buildSequence {
                var i = 1
                while (true){
                    val k = i%7 +1
                    val item = User(1,"fig$k","18255132583","","","http://120.79.250.237:8080/test/fig$k.png","fig$k")
                    i++
                    yield(item)
                }
            }
            mAdapter.setList(users.take(1000).toMutableList())
        }
    }

    override fun getLayoutId() = R.layout.fragment_contact

    @Inject override fun setViewModule(viewModel: MainViewModel) {
        mViewModel = viewModel
    }

}// Required empty public constructor
