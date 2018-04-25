package com.dnnt.touch.ui.register

import com.dnnt.touch.di.FragmentScoped
import com.dnnt.touch.ui.resetpassword.NewPwdFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by dnnt on 18-2-2.
 */
@Module
abstract class RegisterModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun phoneVerificationFragment(): PhoneVerificationFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun registerFragment(): RegisterFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun newPwdFragment(): NewPwdFragment
}