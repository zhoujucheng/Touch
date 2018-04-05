package com.dnnt.touch.learnTest;

import com.dnnt.touch.network.NetService;

import org.junit.Before
import org.junit.Test
import org.mockito.Mock

/**
 * Created by dnnt on 18-3-24.
 */

class LearnTest {
    @Mock
    lateinit var mNetService: NetService

    @Before
    fun setNetService(){
//        when(mNetService.login(new HashMap<>())).thenReturn()
    }

    @Test
    fun testNetService(){
        mNetService.login(hashMapOf())
            .doOnNext {  }
            .subscribe {
                println("test login")
            }
    }
}
