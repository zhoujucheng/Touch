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

    private val mNetService = SingleTon.netService!!
    private var code: Int = 0

    @Test
    fun login(){
        mNetService.login(mapOf(Pair(USER_NAME, "abc"), Pair(PASSWORD, "abc")))
                .subscribe{
                    println(it.body()?.obj?.phone ?: "null")
                    code = it.code()
                }
        assertEquals(200,code)
    }

    @Test
    fun getVerificationCode(){
        mNetService.getVerificationCode("")
                .subscribe({
                    code = it.code()
                })
        assertEquals(200,code)
    }

    @Test
    fun codeVerification(){
        mNetService.codeVerification("123456")
                .subscribe({
                    code = it.code()
                })
        assertEquals(200,code)
    }

}