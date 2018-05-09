package com.dnnt.touch

import com.dnnt.touch.network.NetService
import com.dnnt.touch.util.BASE_URL
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.FileInputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * Created by dnnt on 18-1-26.
 */
object SingleTon {
    val netService = getService(getSSL())

    fun getService(pair:Pair<SSLContext, X509TrustManager>): NetService{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.
                Builder().
                addNetworkInterceptor(HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BASIC))
                .sslSocketFactory(pair.first.socketFactory,pair.second)
                .build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()))
            .build()
            .create(NetService::class.java)
    }

    fun getSSL():Pair<SSLContext, X509TrustManager>{
        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        val caIS = FileInputStream(BuildConfig.JKS_FILE_PATH)
        val certificateFactory = CertificateFactory.getInstance("X.509")
        val ca = certificateFactory.generateCertificate(caIS)
        caIS.close()
        keyStore.setCertificateEntry("tomcat", ca)
        val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
        tmf.init(keyStore)
        val tms = tmf.trustManagers
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tms, null)
        return Pair(sslContext,tms[0] as X509TrustManager)
    }
}