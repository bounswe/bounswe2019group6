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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserViewModel(private val dataSource: UserDao, private val networkSource: RequestService) :
    ViewModel() {

    companion object {
        private var userUpdated = false
    }

    fun fetchUser(): Flowable<User> {
        return networkSource.user().map { UserAdapter.adapt(it) }.toFlowable()
    }

    fun fetchAndUpdateUser(): Completable {
        return networkSource.user().map { UserAdapter.adapt(it) }
            .flatMapCompletable { updateUser(it) }
    }

    fun user(context: Context): Single<User> {

        val single: Single<User>

        if (!userUpdated && isNetworkConnected(context)) {
            val networkUser = networkSource.user().map { UserAdapter.adapt(it) }
                .doOnSuccess {
                    updateUser(it).subscribe()
                    userUpdated = true
                }

            single = networkUser.flatMap { dataSource.getUser() }
        } else {
            single =
                dataSource.getUser().doOnError { ErrorHandler.handleUserViewError(it, context) }
        }

        return single
    }

    fun updateUser(user: User): Completable {
        return dataSource.insertUser(user)
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
    }
}