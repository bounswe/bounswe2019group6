package com.traderx.viewmodel

import androidx.lifecycle.ViewModel
import com.traderx.api.RequestService
import com.traderx.api.response.ArticleSearchResponse
import com.traderx.api.response.AssetsResponse
import com.traderx.api.response.EquipmentSearchResponse
import com.traderx.api.response.FollowerResponse
import com.traderx.db.User
import io.reactivex.Single

class UserViewModel(private val networkSource: RequestService): ViewModel() {
    fun userProfile(username: String): Single<User> {
        return networkSource.userProfile(username)
    }

    fun allUsers(): Single<List<User>> {
        return networkSource.allUsers()
    }

    fun searchUsers(search: String): Single<List<User>> {
        return networkSource.searchUsers(search)
    }

    fun searchArticles(search: String): Single<List<ArticleSearchResponse>> {
        return networkSource.searchArticles(search)
    }

    fun searchEquipments(search: String): Single<List<EquipmentSearchResponse>> {
        return networkSource.searchEquipments(search)
    }

    fun followers(username: String): Single<ArrayList<FollowerResponse>> {
        return networkSource.followersList(username)
    }

    fun followings(username: String): Single<ArrayList<FollowerResponse>> {
        return networkSource.followingsList(username)
    }
}