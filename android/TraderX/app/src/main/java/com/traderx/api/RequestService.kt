package com.traderx.api

import com.traderx.api.request.AlertRequest
import com.traderx.api.request.LoginRequest
import com.traderx.api.request.SignUpRequest
import com.traderx.api.response.*
import com.traderx.db.Article
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

    @POST(ApiEndpoint.USER_FORGOT_PASSWORD)
    fun forgotpassword(@Query("email") email: String): Single<SuccessResponse>

    @GET(ApiEndpoint.USERS_ALL)
    fun allUsers(): Single<List<User>>

    @GET(ApiEndpoint.USER_PROFILE)
    fun userProfile(@Path("username") username: String): Single<User>

    @POST(ApiEndpoint.USER_SIGNOUT)
    fun signout(): Single<SuccessResponse>

    @POST(ApiEndpoint.BECOME_TRADER)
    fun becomeTrader(@Path("role") role: String, @Query("iban") iban: String): Completable

    @POST(ApiEndpoint.FOLLOW_USER)
    fun followUser(@Query("username") username: String): Single<SuccessResponse>

    @POST(ApiEndpoint.UNFOLLOW_USER)
    fun unfollowUser(@Query("username") username: String): Single<SuccessResponse>

    @POST(ApiEndpoint.FOLLOW_REMOVE)
    fun removeFollower(@Query("username") username: String): Single<SuccessResponse>

    @GET(ApiEndpoint.FOLLOWERS_LIST)
    fun followersList(@Query("username") username: String): Single<ArrayList<FollowerResponse>>

    @GET(ApiEndpoint.FOLLOWINGS_LIST)
    fun followingsList(@Query("username") username: String): Single<ArrayList<FollowerResponse>>

    @POST(ApiEndpoint.FOLLOW_ACCEPT)
    fun acceptFollowRequest(@Query("username") username: String): Completable

    @POST(ApiEndpoint.FOLLOW_DECLINE)
    fun declineFollowRequest(@Query("username") username: String): Completable

    @GET(ApiEndpoint.PENDING_FOLLOW_REQUESTS)
    fun pendingFollowRequests(@Query("username") username: String): Single<ArrayList<FollowerResponse>>

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
    fun getEquipment(@Path("name") code: String): Single<EquipmentResponse>

    @GET(ApiEndpoint.EQUIPMENT_CURRENCY_LIST)
    fun getCurrencyEquipments(): Single<EquipmentsResponse>

    @GET(ApiEndpoint.EQUIPMENT_CRYPTO_CURRENCY_LIST)
    fun getCryptoCurrencyEquipments(): Single<EquipmentsResponse>

    @GET(ApiEndpoint.EQUIPMENT_STOCK_LIST)
    fun getStockEquipments(): Single<EquipmentsResponse>

    @GET(ApiEndpoint.COMMENT_EQUIPMENT)
    fun getEquipmentComments(@Path("code") code: String): Single<List<CommentResponse>>

    @POST(ApiEndpoint.TRANSACTION_BUY)
    fun postTransactionBuy(@Query("code") code: String, @Query("amount") amount: Double): Completable

    @GET(ApiEndpoint.TRANSACTIONS)
    fun getTransactions(@Path("username") username: String): Single<List<TransactionsResponse>>

    @GET(ApiEndpoint.ASSET_AMOUNT)
    fun getAssetAmount(@Path("code") code: String): Single<AssetResponse>

    @GET(ApiEndpoint.ALERT_ALL)
    fun getAlerts(): Single<ArrayList<AlertResponse>>

    @POST(ApiEndpoint.ALERT_CREATE)
    fun createAlert(@Body alert: AlertRequest): Completable

    @DELETE(ApiEndpoint.ALERT_DELETE)
    fun deleteAlert(@Query("id") id: Int): Completable

    @POST(ApiEndpoint.ADD_PORTFOLIO)
    fun createPortfolio(@Query("portfolioName")  portfolioName: String): Completable


    @GET(ApiEndpoint.GET_PORTFOLIO)
    fun getPortfolio(@Query("portfolioName") portfolioName: String): Single<PortfolioResponse>

    @POST(ApiEndpoint.ADD_TO_PORTFOLIO)
    fun addToPortfolio(
        @Query("portfolioName") portfolioName: String,
        @Query("code") equipment: String
    ): Completable

    @POST(ApiEndpoint.DELETE_PORTFOLIO)
    fun delPortfolio(@Query("portfolioName") portfolioName: String): Completable

    @POST(ApiEndpoint.DELETE_FROM_PORTFOLIO)
    fun delFromPortfolio(
        @Query("portfolioName") portfolioName: String,
        @Query("code") equipment: String
    ): Completable


}