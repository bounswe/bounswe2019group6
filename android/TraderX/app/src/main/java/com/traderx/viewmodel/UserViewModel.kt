package com.traderx.viewmodel

import androidx.lifecycle.ViewModel
import com.traderx.adapter.UserAdapter
import com.traderx.api.RequestService
import com.traderx.db.User
import com.traderx.db.UserDao
import io.reactivex.Completable
import io.reactivex.Flowable

class UserViewModel(private val dataSource: UserDao, private val networkSource: RequestService) :
    ViewModel() {

    fun fetchUser(): Flowable<User> {
        return networkSource.user().map { UserAdapter.adapt(it) }
    }

    fun fetchAndUpdateUser(): Completable {
        return networkSource.user().map { UserAdapter.adapt(it) }
            .flatMapCompletable { updateUser(it) }
    }

    fun user(): Flowable<User> {
        return dataSource.getUser()
    }

    fun updateUser(user: User): Completable {
        return dataSource.insertUser(user)
    }
}