package com.traderx.viewmodel

import androidx.lifecycle.ViewModel
import com.traderx.api.RequestService
import com.traderx.api.response.FollowerResponse
import com.traderx.api.response.SuccessResponse
import com.traderx.db.User
import io.reactivex.Single

class UserViewModel(private val networkSource: RequestService): ViewModel() {
    fun userProfile(username: String): Single<User> {
        return networkSource.userProfile(username)
    }

    fun allUsers(): Single<List<User>> {
        return networkSource.allUsers()
    }

    fun followers(username: String): Single<ArrayList<FollowerResponse>> {
        return networkSource.followersList(username)
    }

    fun followings(username: String): Single<ArrayList<FollowerResponse>> {
        return networkSource.followingsList(username)
    }
}