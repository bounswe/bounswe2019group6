package com.traderx.viewmodel

import android.content.Context
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

    fun followUser(username: String): Single<SuccessResponse> {
        return networkSource.followUser(username)
    }

    fun unfollowUser(username: String): Single<SuccessResponse> {
        return networkSource.unfollowUser(username)
    }

    fun allUsers(): Single<List<User>> {
        return networkSource.allUsers()
    }

    fun followers(username: String): Single<List<FollowerResponse>> {
        return networkSource.followersList(username)
    }

    fun followings(username: String): Single<List<FollowerResponse>> {
        return networkSource.followingsList(username)
    }
}