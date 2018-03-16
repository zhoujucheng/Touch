package com.dnnt.touch.ui.resetpassword

import com.dnnt.touch.di.FragmentScoped
import com.dnnt.touch.ui.register.PhoneVerificationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by dnnt on 18-2-3.
 */
@Module
abstract class ResetPwdModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun phoneVerificationFragment(): PhoneVerificationFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun newPwdFragment(): NewPwdFragment

}