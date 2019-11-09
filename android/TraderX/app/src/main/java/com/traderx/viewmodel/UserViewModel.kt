package com.traderx.viewmodel

import androidx.lifecycle.ViewModel
import com.traderx.api.RequestService
import com.traderx.api.response.SuccessResponse
import com.traderx.db.User
import io.reactivex.Single

class UserViewModel(private val networkSource: RequestService): ViewModel() {
    fun userProfile(username: String): Single<User> {
        return networkSource.userProfile(username)
    }

    fun followUser(username: String): Single<SuccessResponse> {
        return networkSource.followUser(username)
    }
}