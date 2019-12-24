package com.traderx.viewmodel

import android.content.Context
import android.util.Log
import com.traderx.api.ErrorHandler
import com.traderx.api.RequestService
import com.traderx.api.response.AssetResponse
import com.traderx.api.response.AssetsResponse
import com.traderx.api.response.FollowerResponse
import com.traderx.api.response.SuccessResponse
import com.traderx.db.User
import com.traderx.db.UserDao
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class AuthUserViewModel(
    private val dataSource: UserDao,
    private val networkSource: RequestService
) : BaseViewModel() {

    companion object {
        private var userUpdated = false

        fun refreshUser() {
            userUpdated = false
        }
    }


    fun user(context: Context): Single<User> {

        return if (!userUpdated && isNetworkConnected(context)) {
            val networkUser = fetchUser()
                .doOnSuccess {
                    updateUserLocal(it).subscribe()
                }
            //To make this work, the func should return flowable.
            networkUser.flatMap { localUser() }.subscribeOn(Schedulers.io())
        } else {
            localUser().doOnError { ErrorHandler.handleUserViewError(it, context) }
                .subscribeOn(Schedulers.io())
        }
    }

    fun userOrNew(context: Context): Single<User> {
        return user(context).onErrorReturnItem(User.newInstance(""))
    }

    fun deleteUser(): Completable {
        userUpdated = false

        return dataSource.deleteUser()
    }

    fun updateUser(status: Boolean): Completable {
        userUpdated = false
        return networkSource.updateUser(if (status) "private" else "public")
    }

    fun followUser(username: String): Single<SuccessResponse> {
        refreshUser()

        return networkSource.followUser(username)
    }

    fun unfollowUser(username: String): Single<SuccessResponse> {
        refreshUser()

        return networkSource.unfollowUser(username)
    }

    fun removeFollower(username: String): Single<SuccessResponse> {
        refreshUser()

        return networkSource.removeFollower(username)
    }

    fun pendingFollowRequests(context: Context): Single<ArrayList<FollowerResponse>> {
        return user(context).flatMap { networkSource.pendingFollowRequests(it.username) }
    }

    fun acceptFollowRequest(username: String): Completable {
        refreshUser()

        return networkSource.acceptFollowRequest(username)
    }

    fun declineFollowRequest(username: String): Completable {
        refreshUser()

        return networkSource.declineFollowRequest(username)
    }

    fun becomeTrader(iban: String): Completable {
        refreshUser()

        return networkSource.becomeTrader("trader", iban)
    }

    fun getAssetAmount(code: String): Single<AssetResponse> {
        return networkSource.getAssetAmount(code)
    }

    private fun fetchUser(): Single<User> {
        return networkSource.user()
    }

    private fun localUser(): Single<User> {
        return dataSource.getUser()
    }

    private fun updateUserLocal(user: User): Completable {
        refreshUser()

        return dataSource.insertUser(user)
    }
    fun getAssets(): Single<ArrayList<AssetsResponse>> {
        return networkSource.getAssets()
    }

    fun sellAsset(code : String, amount : Double): Completable {
        return networkSource.postTransactionSell(code, amount)
    }
}