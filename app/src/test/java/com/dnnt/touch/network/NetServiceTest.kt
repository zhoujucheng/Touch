package com.dnnt.touch.network

import com.dnnt.touch.SingleTon
import com.dnnt.touch.been.User
import com.dnnt.touch.util.*
import org.junit.Test
import org.junit.Assert.*
import org.hamcrest.Matchers.*

/**
 * Created by dnnt on 18-1-26.
 */

class NetServiceTest {

    companion object {
        private var token = ""
        private val mNetService = SingleTon.netService
        private val phone = "18255132583"

    }

    private var code: Int = 0
    val successful = 200



    @Test
    fun loginTest(){
        println("loginTest")
        mNetService.login(hashMapOf(Pair(NAME_OR_PHONE, phone), Pair(PASSWORD, "123456")))
                .subscribe({
                    code = it.code
                    token = it.msg
                },{_,_ -> })
        assertEquals(successful,code)

        mNetService.login(hashMapOf(Pair(NAME_OR_PHONE, phone), Pair(PASSWORD, "999999")))
            .subscribe({
            },{_,code ->
                this.code = code
            })

        assertEquals(-1,code)

    }

    @Test
    fun changePasswordTest(){
        mNetService.changePassword(hashMapOf(Pair(ID,"3"), Pair(OLD_PASSWORD,"123456"), Pair(NEW_PASSWORD,"654321")))
            .subscribe ({
                code = it.code
            },{_,_ -> })
        assertEquals(successful,code)
        code = 0

        mNetService.changePassword(hashMapOf(Pair(ID,"3"), Pair(OLD_PASSWORD,"654321"), Pair(NEW_PASSWORD,"123456")))
            .subscribe ({
                code = it.code
            },{_,_ -> })
        assertEquals(successful,code)

        mNetService.changePassword(hashMapOf(Pair(ID,"3"), Pair(OLD_PASSWORD,"654321"), Pair(NEW_PASSWORD,"123456")))
            .subscribe ({},{_,code ->
                this.code = code
            })

        assertEquals(-1,code)
    }

    @Test
    fun getVerificationCodeTest(){
        mNetService.getVerificationCode(phone, CODE_TAG_REGISTER)
            .subscribe({},{_,code ->
                this.code = code
            })
        assertEquals(-1,code)

        code = 0

        mNetService.getVerificationCode("12345678901", CODE_TAG_RESET)
            .subscribe ({},{_,code ->
                this.code = code
            })
        assertEquals(-1,code)

        mNetService.getVerificationCode("18255132584", CODE_TAG_REGISTER)
            .subscribe({
                this.code = it.code
            },{_,_ ->})
        assertEquals(successful,code)

    }

    @Test
    fun getFriendsTest(){
        var list:List<User>? = null
        mNetService.getFriends(token)
            .subscribe({
                code = it.code
                list = it.obj
            },{_,_ ->})

        assertEquals(successful,code)
        assertNotNull(list)
        assertThat(list?.size ?: 0, greaterThan(0))
    }

}