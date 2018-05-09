package com.dnnt.touch.network

import com.dnnt.touch.SingleTon
import com.dnnt.touch.util.*
import org.junit.Test
import org.junit.Assert.*
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters

/**
 * Created by dnnt on 18-1-26.
 */
class NetServiceTest {

    companion object {
        private var token = ""
        private val mNetService = SingleTon.netService

    }

    private var code: Int = 0
    val successful = 200



    @Test
    fun loginTest(){
        println("loginTest")
        mNetService.login(hashMapOf(Pair(NAME_OR_PHONE, "18255132583"), Pair(PASSWORD, "123456")))
                .subscribe({
                    println("onNext:")
                    println(it.body()?.obj?.phone ?: "null")
                    token = it.body()?.msg ?: ""
                    println("token: $token")
                    code = it.code()
                },{
                    println("onError:")
                    it.printStackTrace()
                },{ println("onComplete")},{
                    println("onSubscribe")
//                    it.dispose()
                })
        assertEquals(successful,code)
    }

    @Test
    fun changePasswordTest(){
        mNetService.changePassword(hashMapOf(Pair(ID,"3"), Pair(OLD_PASSWORD,"123456"), Pair(NEW_PASSWORD,"654321")))
            .subscribe {
                code = it.code()
                println("msg: ${it.body()?.msg}")
            }
        assertEquals(successful,code)
        mNetService.changePassword(hashMapOf(Pair(ID,"3"), Pair(OLD_PASSWORD,"654321"), Pair(NEW_PASSWORD,"123456")))
            .subscribe {
                code = it.code()
                println("msg: ${it.body()?.msg}")
            }
        assertEquals(successful,code)
    }

//    @Test
//    fun tokenTest(){
//        println("tokenTest")
//        println("token: $token")
////        Thread.sleep(22000)
//        mNetService.getTest(token)
//            .subscribe {
//                code = it.code()
//                if (!it.isSuccessful){
//                    println("error body:\n${it.errorBody()?.string()}")
//                    println("message: \n{it.message()}")
//                }
//            }
//        assertEquals(successful,code)
//    }
}