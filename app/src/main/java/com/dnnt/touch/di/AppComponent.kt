package com.dnnt.touch.di

import com.dnnt.touch.MyApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

/**
 * Created by dnnt on 17-12-23.
 */
@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class,
        ActivityBindingModule::class])
interface AppComponent : AndroidInjector<MyApplication> {

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application: MyApplication): Builder

        fun build(): AppComponent
    }
}