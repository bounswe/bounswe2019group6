package com.traderx.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.traderx.api.RequestService
import com.traderx.db.UserDao

class AuthUserViewModelFactory(private val dataSource: UserDao, private val networkSource: RequestService) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AuthUserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthUserViewModel(dataSource, networkSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}