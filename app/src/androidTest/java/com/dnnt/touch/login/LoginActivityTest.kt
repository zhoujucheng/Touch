package com.dnnt.touch.login

import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import com.dnnt.touch.R
import org.junit.Rule
import android.support.test.rule.ActivityTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import com.dnnt.touch.ui.login.LoginActivity
import org.hamcrest.Matchers.not

/**
 * Created by dnnt on 18-1-26.
 */
@RunWith(AndroidJUnit4::class)
class LoginActivityTest{

    @get:Rule
    val mActivityTestRule = ActivityTestRule<LoginActivity>(LoginActivity::class.java)

//    @Before
//    fun before(){
//        mActivityTestRule.launchActivity(Intent())
//    }

    @Test(timeout = 10000)
    fun login(){
        onView(withId(R.id.user_phone)).perform(typeText("18255132583"))
        onView(withId(R.id.password)).perform(typeText("123456"))
        onView(withId(R.id.login_btn)).perform(click())
    }
}