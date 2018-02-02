package com.dnnt.touch.network

import com.dnnt.touch.SingleTon
import com.dnnt.touch.util.PASSWORD
import com.dnnt.touch.util.USER_NAME
import org.junit.Test
import org.junit.Assert.*

/**
 * Created by dnnt on 18-1-26.
 */
class NetServiceTest {
    @Test
    fun login(){
        SingleTon.netService.login(mapOf(Pair(USER_NAME, "abc"), Pair(PASSWORD, "abc")))
                .subscribe{
                    println(it.body()?.obj?.phone ?: "null")
                    assertEquals(true, it.isSuccessful)
                }
    }
}