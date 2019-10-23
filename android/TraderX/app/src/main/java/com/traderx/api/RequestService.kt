package com.traderx.api

import com.traderx.AppConfig
import com.traderx.api.request.LoginRequest
import com.traderx.api.request.SignUpRequest
import com.traderx.api.response.SuccessResponse
import com.traderx.api.response.TokenResponse
import com.traderx.api.response.UserAll
import com.traderx.api.response.UserResponse
import io.reactivex.Flowable
import io.reactivex.Single
import org.intellij.lang.annotations.Flow
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

class ApiUri {
    companion object {
        const val API_URI: String = AppConfig.API_HOST
        const val USER_URI: String = "$API_URI/users"
        const val USERS_GET_ALL: String = "$USER_URI/getAll"
        const val USER_SIGNIN: String = "$API_URI/login"
        const val USER_SINGUP: String = "$API_URI/signup"
        const val USER_INFO: String = "$USER_URI/me"
    }
}

interface RequestService {
    @GET(ApiUri.USER_INFO)
    fun user(): Single<UserResponse>

    @POST(ApiUri.USER_SIGNIN)
    fun login(@Body loginRequest: LoginRequest): Single<TokenResponse>

    @POST(ApiUri.USER_SINGUP)
    fun register(@Body signUpRequest: SignUpRequest): Single<SuccessResponse>

    @GET(ApiUri.USERS_GET_ALL)
    fun usersGetAll(): Single<List<UserAll>>
}