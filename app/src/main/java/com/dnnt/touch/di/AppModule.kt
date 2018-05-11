package com.dnnt.touch.di

import android.content.Context
import com.dnnt.touch.MyApplication
import com.dnnt.touch.base.AlreadyInRequestException
import com.dnnt.touch.network.NetService
import com.dnnt.touch.receiver.NetworkReceiver
import com.dnnt.touch.util.BASE_URL
import com.dnnt.touch.base.MyScheduler
import com.dnnt.touch.base.NetworkNotAvailableException
import com.dnnt.touch.util.debugOnly
import com.dnnt.touch.util.getSSL
import com.dnnt.touch.util.logi
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Singleton

/**
 * Created by dnnt on 17-12-23.
 */
@Module
class AppModule {

    @Provides
    fun context(application: MyApplication): Context = application


    @Provides
    @Singleton
    fun provideCachedThreadPool(): ExecutorService = Executors.newCachedThreadPool()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, scheduler: MyScheduler) : Retrofit{
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(scheduler))
                .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(executorService: ExecutorService): OkHttpClient{
        val pair = getSSL()
        val clientBuilder = OkHttpClient.Builder()
            //网络判断拦截器
            .addInterceptor{
                if (!NetworkReceiver.isNetUsable()){
                    throw NetworkNotAvailableException()
                }
                return@addInterceptor it.proceed(it.request())
            }
            .addInterceptor(object : Interceptor{
                private val set = hashSetOf<String>()
                override fun intercept(chain: Interceptor.Chain): Response {
                    val url = chain.request().url().toString()
                    logi("intercept url",url)
                    if (url.endsWith(".png") || url.endsWith("user/uploadErrFile")){
                        return chain.proceed(chain.request())
                    }
                    if (set.contains(url)){
                        logi("OkHttpInterceptor","throw AlreadyInRequestException")
                        throw AlreadyInRequestException()
                    }else{
                        set.add(url)
                        val response = chain.proceed(chain.request())
                        set.remove(url)
                        return response
                    }
                }

            })
            .dispatcher(Dispatcher(executorService))
            .sslSocketFactory(pair.first.socketFactory,pair.second)
        //在debug时，为okhttp设置日志拦截器
        debugOnly {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addNetworkInterceptor(loggingInterceptor)
        }
        return clientBuilder.build()
    }

    @Provides
    fun provideNetService(retrofit: Retrofit): NetService = retrofit.create(NetService::class.java)


}