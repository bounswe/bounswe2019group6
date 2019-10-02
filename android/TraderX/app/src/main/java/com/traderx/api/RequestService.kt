package com.traderx.api

import com.traderx.api.response.LoginResponse
import com.traderx.api.response.UserResponse
import com.traderx.db.User
import io.reactivex.Flowable
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface RequestService {
    @GET("user/info")
    fun user(): Flowable<UserResponse>
    @FormUrlEncoded
    @POST("auth/login")
    fun login(email: String, password: String): Flowable<LoginResponse>
}