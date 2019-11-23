package com.traderx.viewmodel

import android.content.Context
import com.traderx.api.ErrorHandler
import com.traderx.api.RequestService
import com.traderx.api.response.FollowerResponse
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

    fun deleteUser(): Completable {
        userUpdated = false

        return dataSource.deleteUser()
    }

    fun updateUser(status: Boolean): Completable {
        userUpdated = false
        return networkSource.updateUser(if(status) "private" else "public")
    }


    fun pendingFollowRequests(context: Context): Single<List<FollowerResponse>> {
        return user(context).flatMap { networkSource.pendingFollowRequests(it.username) }
    }

    private fun fetchUser(): Single<User> {
        return networkSource.user()
    }

    private fun localUser(): Single<User> {
        return dataSource.getUser()
    }

    private fun updateUserLocal(user: User): Completable {
        userUpdated = true

        return dataSource.insertUser(user)
    }
}