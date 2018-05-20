package com.dnnt.touch.login

import android.content.ComponentName
import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.intent.Intents.*
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.dnnt.touch.R
import org.junit.Rule
import org.junit.Assert.*
import android.support.test.espresso.matcher.ViewMatchers.*
import com.dnnt.touch.ui.login.LoginActivity
import com.dnnt.touch.ui.main.MainActivity
import android.support.test.InstrumentationRegistry.getTargetContext
import android.support.test.espresso.intent.rule.IntentsTestRule


/**
 * Created by dnnt on 18-1-26.
 */
@RunWith(AndroidJUnit4::class)
class LoginActivityTest{

    @get:Rule
    val mActivityTestRule = IntentsTestRule<LoginActivity>(LoginActivity::class.java)

    @Test
    fun login(){
        onView(withId(R.id.name_or_phone)).perform(clearText(),typeText("18255132583"))
        onView(withId(R.id.password)).perform(clearText(),typeText("123456"))
        val activity = mActivityTestRule.activity

        onView(withId(R.id.login)).perform(click())

        assertTrue(activity.isFinishing)

        intended(
            hasComponent(
                ComponentName(getTargetContext(), MainActivity::class.java)
            )
        )
    }
}