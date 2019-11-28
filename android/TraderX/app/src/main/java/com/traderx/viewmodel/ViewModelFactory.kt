package com.traderx.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.traderx.api.RequestService
import com.traderx.db.ArticleDao
import com.traderx.db.EquipmentDao
import com.traderx.db.UserDao

class ViewModelFactory(private val dataSource: Any, private val networkSource: RequestService) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(AuthUserViewModel::class.java) && dataSource is UserDao -> AuthUserViewModel(
                dataSource,
                networkSource
            ) as T
            modelClass.isAssignableFrom(UserViewModel::class.java) -> UserViewModel(networkSource) as T
            modelClass.isAssignableFrom(ArticleViewModel::class.java) && dataSource is ArticleDao -> ArticleViewModel(
                dataSource,
                networkSource
            ) as T
            modelClass.isAssignableFrom(EquipmentViewModel::class.java) && dataSource is EquipmentDao -> EquipmentViewModel(
                dataSource,
                networkSource
            ) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class or dataSource")
        }
    }
}