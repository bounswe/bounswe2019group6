package com.traderx.viewmodel

import com.traderx.api.RequestService
import com.traderx.api.response.TransactionsResponse
import com.traderx.db.Article
import com.traderx.db.ArticleDao
import com.traderx.db.TransactionDao
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class TransactionViewModel(
    private val dataSource: TransactionDao,
    private val networkSource: RequestService
) : BaseViewModel() {
    fun makeTransaction(code: String, amount: Double): Completable {
        return networkSource.postTransactionBuy(code, amount)
    }

    fun getTransactions(username: String): Single<List<TransactionsResponse>> {
        return networkSource.getTransactions(username)
    }
}