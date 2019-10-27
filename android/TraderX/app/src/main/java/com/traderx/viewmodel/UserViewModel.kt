package com.traderx.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import com.traderx.adapter.UserAdapter
import com.traderx.api.ErrorHandler
import com.traderx.api.RequestService
import com.traderx.db.User
import com.traderx.db.UserDao
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class UserViewModel(private val dataSource: UserDao, private val networkSource: RequestService) :
    ViewModel() {

    companion object {
        private var userUpdated = false
    }

    fun fetchUser(): Single<User> {
        return networkSource.user().map { UserAdapter.adapt(it) }
    }

    fun user(context: Context): Single<User> {

        val single: Single<User>

        if (!userUpdated && isNetworkConnected(context)) {
            val networkUser = fetchUser()
                .doOnSuccess {
                    updateUser(it).subscribe()
                }

            single = networkUser.flatMap { dataSource.getUser() }
        } else {
            single =
                dataSource.getUser().doOnError { ErrorHandler.handleUserViewError(it, context) }
        }

        return single
    }

    fun deleteUser(): Completable {
        userUpdated = false

        return dataSource.deleteUser()
    }

    fun updateUser(user: User): Completable {
        userUpdated = true

        return dataSource.insertUser(user)
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
    }
}