package com.dnnt.touch.network

import com.dnnt.touch.been.Json
import com.dnnt.touch.been.User
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by dnnt on 18-1-26.
 */

interface NetService {
    @POST("login")
    @FormUrlEncoded
    fun login(@FieldMap map: Map<String,String>): Observable<Response<Json<User>>>
}