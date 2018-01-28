package com.dnnt.touch.di

import com.dnnt.touch.MyApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by dnnt on 17-12-23.
 */
@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class,
        ActivityBindingModule::class])
interface AppComponent : AndroidInjector<MyApplication> {

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application: MyApplication): Builder

        fun build(): AppComponent
    }
}