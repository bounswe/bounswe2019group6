package com.traderx.viewmodel

import com.traderx.api.RequestService
import com.traderx.api.response.PortfolioResponse
import com.traderx.db.Equipment
import io.reactivex.Completable
import io.reactivex.Single

class PortfolioViewModel(
    private val networkSource: RequestService
) : BaseViewModel() {

    fun addPortfolio(name: String): Completable {
        return networkSource.createPortfolio(name)
    }

    fun addToPortfolio(name: String, codes: List<String>): Completable {
        return networkSource.addToPortfolio(name, codes)
    }

    fun getPortfolios(): Single<ArrayList<PortfolioResponse>> {
        return networkSource.getPortfolios()
    }

    fun getPortfolio(name: String): Single<List<Equipment>> {
        return networkSource.getPortfolio(name)
    }

    fun deletePortfolio(name: String): Completable {
        return networkSource.delPortfolio(name)
    }

    fun deleteFromPortfolio(name: String, code: String): Completable {
        return networkSource.delFromPortfolio(name, code)
    }

}