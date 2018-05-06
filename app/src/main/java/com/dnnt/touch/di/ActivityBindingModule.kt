package com.dnnt.touch.di

import com.dnnt.touch.ui.changepassword.ChangePwdActivity
import com.dnnt.touch.ui.chat.ChatActivity
import com.dnnt.touch.ui.login.LoginActivity
import com.dnnt.touch.ui.login.LoginModule
import com.dnnt.touch.ui.main.MainActivity
import com.dnnt.touch.ui.main.MainModule
import com.dnnt.touch.ui.register.RegisterActivity
import com.dnnt.touch.ui.register.RegisterModule
import com.dnnt.touch.ui.resetpassword.ResetPwdActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by dnnt on 17-12-23.
 */
@Module
abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun loginActivity(): LoginActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [RegisterModule::class])
    abstract fun registerActivity(): RegisterActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [RegisterModule::class])
    abstract fun resetPwdActivity(): ResetPwdActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun mainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector()
    abstract fun chatActivity(): ChatActivity

    @ActivityScoped
    @ContributesAndroidInjector()
    abstract fun changePwdActivity(): ChangePwdActivity
}