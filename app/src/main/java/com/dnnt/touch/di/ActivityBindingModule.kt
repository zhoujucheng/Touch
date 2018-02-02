package com.dnnt.touch.di

import com.dnnt.touch.ui.login.LoginActivity
import com.dnnt.touch.ui.login.LoginModule
import com.dnnt.touch.ui.register.RegisterActivity
import com.dnnt.touch.ui.register.RegisterModule
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
}