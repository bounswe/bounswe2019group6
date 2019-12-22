package com.traderx.api

import com.traderx.api.request.*
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
    fun getArticle(@Path("id") articleId: Int): Single<Article>

    @GET(ApiEndpoint.ARTICLES)
    fun getArticles(): Single<ArrayList<Article>>

    @POST(ApiEndpoint.ARTICLE_CREATE)
    fun createArticle(@Body article: ArticleRequest): Completable

    @POST(ApiEndpoint.ARTICLE_EDIT)
    fun editArticle(@Query("id") id: Int, @Body article: ArticleRequest): Completable

    @POST(ApiEndpoint.ARTICLE_DELETE)
    fun deleteArticle(@Query("id") articleId: Int): Completable

    @GET(ApiEndpoint.ARTICLE_USER_ARTICLES)
    fun getUserArticles(@Path("username") username: String): Single<ArrayList<Article>>

    @GET(ApiEndpoint.COMMENT_ARTICLE)
    fun getArticleComments(@Path("id") id: Int): Single<ArrayList<CommentResponse>>

    @POST(ApiEndpoint.COMMENT_ARTICLE_INSERT)
    fun createCommentArticle(@Path("id") id: Int, @Body comment: CommentRequest): Single<CommentResponse>

    @POST(ApiEndpoint.COMMENT_ARTICLE_EDIT)
    fun editCommentArticle(@Path("id") id: Int, @Body comment: CommentRequest): Completable

    @POST(ApiEndpoint.COMMENT_ARTICLE_VOTE)
    fun voteCommentArticle(@Path("id") id: Int, @Path("type") vote: String): Completable

    @DELETE(ApiEndpoint.COMMENT_ARTICLE_REVOKE)
    fun revokeCommentArticle(@Path("id") id: Int): Completable

    @DELETE(ApiEndpoint.COMMENT_ARTICLE_DELETE)
    fun deleteCommentArticle(@Path("id") id: Int): Completable

    @GET(ApiEndpoint.EQUIPMENT)
    fun getEquipment(@Path("name") code: String): Single<EquipmentResponse>

    @GET(ApiEndpoint.EQUIPMENT_CURRENCY_LIST)
    fun getCurrencyEquipments(): Single<EquipmentsResponse>

    @GET(ApiEndpoint.EQUIPMENT_CRYPTO_CURRENCY_LIST)
    fun getCryptoCurrencyEquipments(): Single<EquipmentsResponse>

    @GET(ApiEndpoint.EQUIPMENT_STOCK_LIST)
    fun getStockEquipments(): Single<EquipmentsResponse>

    @GET(ApiEndpoint.COMMENT_EQUIPMENT)
    fun getEquipmentComments(@Path("code") code: String): Single<ArrayList<CommentResponse>>

    @POST(ApiEndpoint.COMMENT_EQUIPMENT_POST)
    fun createComment(@Path("code") code: String, @Body comment: CommentRequest): Single<CommentResponse>

    @POST(ApiEndpoint.COMMENT_EDIT)
    fun editComment(@Path("id") id: Int, @Body comment: CommentRequest): Completable

    @POST(ApiEndpoint.COMMENT_VOTE)
    fun voteComment(@Path("id") id: Int, @Path("vote") vote: String): Completable

    @DELETE(ApiEndpoint.COMMENT_REVOKE)
    fun revokeComment(@Path("id") id: Int): Completable

    @POST(ApiEndpoint.TRANSACTION_BUY)
    fun postTransactionBuy(@Query("code") code: String, @Query("amount") amount: Double): Completable

    @DELETE(ApiEndpoint.COMMENT_DELETE)
    fun deleteComment(@Path("id") id: Int): Completable

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
    fun createPortfolio(@Query("portfolioName") portfolioName: String): Completable


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

    @GET(ApiEndpoint.EVENTS)
    fun getEvents(): Single<List<EventResponse>>
}