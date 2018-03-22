package com.dnnt.touch.ui.login

import com.dnnt.touch.SingleTon
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by dnnt on 18-1-28.
 */
class LoginViewModelTest {

    @Mock
    private lateinit var mLoginVM: LoginViewModel

    @Before
    fun initMock(){
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun login() {
        mLoginVM.login("18255132583" , "abcdefg")
    }

}