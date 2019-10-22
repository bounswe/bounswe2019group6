package com.traderx.api

import com.traderx.AppConfig
import com.traderx.api.request.SignUpRequest
import com.traderx.api.response.SuccessResponse
import com.traderx.api.response.TokenResponse
import com.traderx.api.response.UserAll
import com.traderx.api.response.UserResponse
import io.reactivex.Flowable
import org.intellij.lang.annotations.Flow
import retrofit2.http.*

class ApiUri {
    companion object {
        const val API_URI: String = AppConfig.API_HOST
        const val USER_URI: String = "$API_URI/users"
        const val USERS_GET_ALL: String = "$USER_URI/getAll"
        const val USER_SIGNIN: String = "$API_URI/signin"
        const val USER_SINGUP: String = "$API_URI/signup"
        const val USER_INFO: String = "$USER_URI/me"
    }
}

interface RequestService {
    @GET(ApiUri.USER_INFO)
    fun user(): Flowable<UserResponse>

    @FormUrlEncoded
    @POST(ApiUri.USER_SIGNIN)
    fun login(@Field("username") username: String, @Field("password") password: String): Flowable<TokenResponse>

    @POST(ApiUri.USER_SINGUP)
    fun register(@Body signUpRequest: SignUpRequest): Flowable<SuccessResponse>

    @GET(ApiUri.USERS_GET_ALL)
    fun usersGetAll(): Flowable<List<UserAll>>
}