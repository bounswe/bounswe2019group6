package com.traderx.util

import android.content.Context
import com.traderx.api.ApiService
import com.traderx.db.*
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

    fun provideEquipmentViewModelFactory(context: Context): ViewModelFactory {
        val dataSource = provideEquipmentDao(context)
        val networkSource = ApiService.getInstance(context)

        return ViewModelFactory(dataSource, networkSource)
    }

    fun provideTransactionViewModelFactory(context: Context): ViewModelFactory {
        val dataSource = provideTransactionDao(context)
        val networkSource = ApiService.getInstance(context)

        return ViewModelFactory(dataSource, networkSource)
    }

    private fun provideUserDao(context: Context): UserDao {
        return AppDatabase.getInstance(context).userDao()
    }

    private fun provideArticleDao(context: Context): ArticleDao {
        return AppDatabase.getInstance(context).articleDao()
    }

    private fun provideEquipmentDao(context: Context): EquipmentDao {
        return AppDatabase.getInstance(context).equipmentDao()
    }

    private fun provideTransactionDao(context: Context): TransactionDao {
        return AppDatabase.getInstance(context).transactionDao()
    }
}