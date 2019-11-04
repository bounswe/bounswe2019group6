package com.traderx.api

import com.traderx.AppConfig
import com.traderx.api.request.LoginRequest
import com.traderx.api.request.SignUpRequest
import com.traderx.api.response.SuccessResponse
import com.traderx.api.response.TokenResponse
import com.traderx.api.response.UserAll
import com.traderx.api.response.UserResponse
import com.traderx.db.User
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

object ApiUri {
    const val API_URI: String = AppConfig.API_HOST
    const val FOLLOW_URI: String = "$API_URI/follow"
    const val FOLLOW_USER: String = "$FOLLOW_URI/follow_user"
    const val USER_URI: String = "$API_URI/users"
    const val USERS_GET_ALL: String = "$USER_URI/getAll"
    const val USER_SIGNIN: String = "$API_URI/login"
    const val USER_SIGNUP: String = "$API_URI/signup"
    const val USER_SIGNOUT: String = "$API_URI/signout"
    const val USER_INFO: String = "$USER_URI/me"
    const val USER_PROFILE: String = "$USER_URI/profile/{username}"
    const val UPDATE_USER: String = "$USER_URI/set_profile/{status}"

}

interface RequestService {
    @GET(ApiUri.USER_INFO)
    fun user(): Single<UserResponse>

    @POST(ApiUri.USER_SIGNIN)
    fun login(@Body loginRequest: LoginRequest): Single<TokenResponse>

    @POST(ApiUri.USER_SIGNUP)
    fun register(@Body signUpRequest: SignUpRequest): Single<SuccessResponse>

    @GET(ApiUri.USERS_GET_ALL)
    fun usersGetAll(): Single<List<UserAll>>

    @GET(ApiUri.USER_PROFILE)
    fun userProfile(@Path("username") username: String): Single<User>

    @POST(ApiUri.USER_SIGNOUT)
    fun signout(): Single<SuccessResponse>

    @POST(ApiUri.FOLLOW_USER)
    fun followUser(@Query("usernameToFollow") username: String): Single<SuccessResponse>

    @POST(ApiUri.UPDATE_USER)
    fun updateUser(@Path("status") status: String): Completable
}