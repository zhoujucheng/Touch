package com.dnnt.touch.ui.login

import com.dnnt.touch.di.ActivityScoped
import dagger.Binds
import dagger.Module

/**
 * Created by dnnt on 18-1-25.
 */
@Module
abstract class LoginModule {
//    @FragmentScoped
//    @ContributesAndroidInjector
//    internal abstract fun tasksFragment(): TasksFragment

    @ActivityScoped
    @Binds abstract fun loginPresenter(presenter: LoginPresenter): LoginContract.Presenter
}