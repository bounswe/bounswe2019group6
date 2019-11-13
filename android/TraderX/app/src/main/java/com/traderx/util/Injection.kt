package com.traderx.util

import android.content.Context
import com.traderx.api.ApiService
import com.traderx.db.AppDatabase
import com.traderx.db.ArticleDao
import com.traderx.db.EquipmentDao
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

    fun provideTraderEquipmentViewModelFactory(context: Context): ViewModelFactory {
        val dataSource = provideTraderEquipmentDao(context)
        val networkSource = ApiService.getInstance(context)

        return ViewModelFactory(dataSource, networkSource)
    }

    private fun provideUserDao(context: Context): UserDao {
        return AppDatabase.getInstance(context).userDao()
    }

    private fun provideArticleDao(context: Context): ArticleDao {
        return AppDatabase.getInstance(context).articleDao()
    }

    private fun provideTraderEquipmentDao(context: Context): EquipmentDao {
        return AppDatabase.getInstance(context).traderEquipmentDao()
    }
}