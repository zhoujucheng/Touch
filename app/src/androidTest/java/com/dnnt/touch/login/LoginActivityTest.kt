package com.dnnt.touch.login

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.Observer
import android.arch.lifecycle.OnLifecycleEvent
import android.content.ComponentName
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.intent.Intents.*
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.dnnt.touch.R
import org.junit.Rule
import org.junit.Assert.*
import android.support.test.rule.ActivityTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import com.dnnt.touch.ui.login.LoginActivity
import com.dnnt.touch.ui.main.MainActivity
import org.junit.After
import android.support.test.InstrumentationRegistry.getTargetContext
import android.support.test.espresso.intent.rule.IntentsTestRule
import com.raizlabs.android.dbflow.kotlinextensions.async
import io.reactivex.ObservableEmitter
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.coroutines.experimental.CoroutineContext


/**
 * Created by dnnt on 18-1-26.
 */
@RunWith(AndroidJUnit4::class)
class LoginActivityTest{

    private var idlingRes: LoginIdlingRes? = null

    @get:Rule
    val mActivityTestRule = IntentsTestRule<LoginActivity>(LoginActivity::class.java)

//    @get:Rule
//    val mActivityTestRule = ActivityTestRule<LoginActivity>(LoginActivity::class.java)

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