package com.traderx.viewmodel

import android.content.Context
import com.traderx.api.ErrorHandler
import com.traderx.api.RequestService
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

    fun fetchUser(): Single<User> {
        return networkSource.user()
    }

    fun user(context: Context): Single<User> {

        return if (!userUpdated && isNetworkConnected(context)) {
            val networkUser = fetchUser()
                .doOnSuccess {
                    updateUserLocal(it).subscribe()
                }
            //Not working as expected
            networkUser.flatMap { dataSource.getUser() }.subscribeOn(Schedulers.io())
        } else {
            dataSource.getUser().doOnError { ErrorHandler.handleUserViewError(it, context) }
                .subscribeOn(Schedulers.io())
        }
    }

    fun deleteUser(): Completable {
        userUpdated = false

        return dataSource.deleteUser()
    }

    fun updateUser(user: User): Completable {
        return networkSource.updateUser(if (user.isPrivate) "private" else "public")
            .andThen { updateUserLocal(user) }
    }

    private fun updateUserLocal(user: User): Completable {
        userUpdated = true

        return dataSource.insertUser(user)
    }
}