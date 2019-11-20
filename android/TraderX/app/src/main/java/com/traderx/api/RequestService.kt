package com.traderx.api

import com.traderx.api.request.LoginRequest
import com.traderx.api.request.SignUpRequest
import com.traderx.api.response.EquipmentResponse
import com.traderx.api.response.FollowerResponse
import com.traderx.api.response.SuccessResponse
import com.traderx.api.response.TokenResponse
import com.traderx.db.Article
import com.traderx.db.Equipment
import com.traderx.db.User
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*


interface RequestService {
    @GET(ApiEndpoint.USER_INFO)
    fun user(): Single<User>

    @POST(ApiEndpoint.USER_SIGNIN)
    fun login(@Body loginRequest: LoginRequest): Single<TokenResponse>

    @POST(ApiEndpoint.USER_SIGNUP)
    fun register(@Body signUpRequest: SignUpRequest): Single<SuccessResponse>

    @GET(ApiEndpoint.USERS_ALL)
    fun allUsers(): Single<List<User>>

    @GET(ApiEndpoint.USER_PROFILE)
    fun userProfile(@Path("username") username: String): Single<User>

    @POST(ApiEndpoint.USER_SIGNOUT)
    fun signout(): Single<SuccessResponse>

    @POST(ApiEndpoint.FOLLOW_USER)
    fun followUser(@Query("username") username: String): Single<SuccessResponse>

    @POST(ApiEndpoint.UNFOLLOW_USER)
    fun unfollowUser(@Query("username") username: String): Single<SuccessResponse>

    @GET(ApiEndpoint.FOLLOWERS_LIST)
    fun followersList(@Query("username") username: String): Single<List<FollowerResponse>>

    @GET(ApiEndpoint.FOLLOWINGS_LIST)
    fun followingsList(@Query("username") username: String): Single<List<FollowerResponse>>

    @GET(ApiEndpoint.PENDING_FOLLOW_REQUESTS)
    fun pendingFollowRequests(@Query("username") username: String): Single<List<FollowerResponse>>

    @POST(ApiEndpoint.UPDATE_USER)
    fun updateUser(@Path("status") status: String): Completable

    @GET(ApiEndpoint.ARTICLE)
    fun getArticle(@Path("articleId") articleId: Int): Single<Article>

    @GET(ApiEndpoint.ARTICLES)
    fun getArticles(): Single<List<Article>>

    @POST(ApiEndpoint.INSERT_ARTICLE)
    fun insertArticle(@Body article: Article): Completable

    @DELETE(ApiEndpoint.INSERT_ARTICLE)
    fun deleteArticle(articleId: Int): Completable

    @GET(ApiEndpoint.EQUIPMENT)
    fun getEquipment(@Path("name") name: String): Single<Equipment>

    @GET(ApiEndpoint.EQUIPMENT_CURRENCY_LIST)
    fun getCurrencyEquipments(): Single<EquipmentResponse>

    @GET(ApiEndpoint.EQUIPMENT_CRYPTO_CURRENCY_LIST)
    fun getCryptoCurrencyEquipments(): Single<EquipmentResponse>

    @GET(ApiEndpoint.EQUIPMENT_STOCK_LIST)
    fun getStockEquipments(): Single<EquipmentResponse>
}