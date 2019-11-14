package com.traderx.util

import android.content.Context
import com.traderx.api.ApiService
import com.traderx.db.AppDatabase
import com.traderx.db.ArticleDao
import com.traderx.db.UserDao
import com.traderx.viewmodel.ViewModelFactory

object Injection {

    fun provideAuthUserViewModelFactory(context: Context): ViewModelFactory {
        val dataSource = provideUserDao(context)
        val networkSource = ApiService.getInstance(context)

        return ViewModelFactory(dataSource, networkSource)
    }

    fun provideUserViewModelFactory(context: Context): ViewModelFactory {
        val networkSource = ApiService.getInstance(context)
        val dataSource = provideUserDao(context)

        return ViewModelFactory(dataSource, networkSource)
    }

    fun provideArticleUserViewModelFactory(context: Context): ViewModelFactory {
        val dataSource = provideArticleDao(context)
        val networkSource = ApiService.getInstance(context)

        return ViewModelFactory(dataSource, networkSource)
    }

    private fun provideUserDao(context: Context): UserDao {
        val database = AppDatabase.getInstance(context)

        return database.userDao()
    }

    private fun provideArticleDao(context: Context): ArticleDao {
        val database = AppDatabase.getInstance(context)

        return database.articleDao()
    }
}