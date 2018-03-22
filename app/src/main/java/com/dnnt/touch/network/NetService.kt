package com.dnnt.touch.network

import com.dnnt.touch.been.Json
import com.dnnt.touch.been.User
import com.dnnt.touch.util.PHONE
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by dnnt on 18-1-26.
 */

interface NetService {
    @POST("user/login")
    @FormUrlEncoded
    fun login(@FieldMap map: Map<String,String>): Observable<Response<Json<User>>>

    @POST("user/getVerificationCode")
    @FormUrlEncoded
    fun getVerificationCode(@Field(PHONE) phone: String): Observable<Response<Json<String>>>

    @POST("user/codeVerification")
    @FormUrlEncoded
    fun codeVerification(@Field("verificationCode") verificationCode: String): Observable<Response<Json<String>>>

    @POST("user/register")
    @FormUrlEncoded
    fun register(@FieldMap map: Map<String, String>): Observable<Response<Json<String>>>

    @POST
    @FormUrlEncoded
    fun resetPassword(@Field("newPassword") newPassword: String): Observable<Response<Json<String>>>

    @GET("user/test")
    fun getTest(): Observable<String>
}