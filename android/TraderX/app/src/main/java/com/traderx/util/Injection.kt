package com.traderx.util

import android.content.Context
import com.traderx.api.RequestServiceFactory
import com.traderx.db.AppDatabase
import com.traderx.db.UserDao
import com.traderx.viewmodel.UserViewModelFactory

object Injection {

    fun provideUserDao(context: Context): UserDao {
        val database = AppDatabase.getInstance(context)
        return database.userDao()
    }

    fun provideUserViewModelFactory(context: Context): UserViewModelFactory {
        val dataSource = provideUserDao(context)
        val networkSource = RequestServiceFactory().provideRequestService()
        return UserViewModelFactory(dataSource, networkSource)
    }
}