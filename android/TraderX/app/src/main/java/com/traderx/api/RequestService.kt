package com.traderx.api

import com.traderx.api.request.RegisterRequest
import com.traderx.api.response.TokenResponse
import com.traderx.api.response.UserResponse
import io.reactivex.Flowable
import retrofit2.http.*


interface RequestService {
    @GET(ApiUri.USER_INFO)
    fun user(): Flowable<UserResponse>

    @FormUrlEncoded
    @POST(ApiUri.USER_SIGNIN)
    fun login(@Field("username") username: String, @Field("password") password: String): Flowable<TokenResponse>

    @POST(ApiUri.USER_SINGUP)
    fun register(@Body registerRequest: RegisterRequest): Flowable<TokenResponse>

}