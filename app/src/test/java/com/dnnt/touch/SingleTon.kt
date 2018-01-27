package com.dnnt.touch

import com.dnnt.touch.network.NetService
import com.dnnt.touch.util.BASE_URL
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by dnnt on 18-1-26.
 */
object SingleTon {
    val netService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.
                    Builder().
                    addNetworkInterceptor(HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BASIC))
                    .build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create()))
            .build()
            .create(NetService::class.java)
}