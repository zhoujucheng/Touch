package com.dnnt.touch.network

import com.dnnt.touch.been.Json
import com.dnnt.touch.been.User
import com.dnnt.touch.util.COOKIE
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
    fun getVerificationCode(@Field(PHONE) phone: String): Observable<Response<Json<Unit>>>

    @POST("user/codeVerification")
    @FormUrlEncoded
    fun codeVerification(@Field("phone") phone: String, @Field("verificationCode") verificationCode: String,@Header(COOKIE) cookie: String): Observable<Response<Json<Unit>>>

    @POST("user/register")
    @FormUrlEncoded
    fun register(@FieldMap map: Map<String, String>,@Header(COOKIE) cookie: String): Observable<Response<Json<Unit>>>

    @POST("user/getFriends")
    @FormUrlEncoded
    fun getFriends(@Field("token") token: String): Observable<Response<Json<List<User>>>>

    @POST
    @FormUrlEncoded
    fun resetPassword(@Field("newPassword") newPassword: String): Observable<Response<Json<String>>>

    @GET("user/test")
    fun getTest(): Observable<String>
}