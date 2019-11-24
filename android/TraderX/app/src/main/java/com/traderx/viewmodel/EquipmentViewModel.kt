package com.traderx.viewmodel

import com.traderx.api.RequestService
import com.traderx.api.response.CommentResponse
import com.traderx.api.response.EquipmentResponse
import com.traderx.api.response.EquipmentsResponse
import com.traderx.db.EquipmentDao
import io.reactivex.Flowable
import io.reactivex.Single

class EquipmentViewModel(
    private val dataSource: EquipmentDao,
    private val networkSource: RequestService
) : BaseViewModel() {

    fun getCurrencyEquipments(): Flowable<EquipmentsResponse> {
        return networkSource.getCurrencyEquipments().map { it }.toFlowable()
    }

    fun getCryptoCurrencyEquipments(): Flowable<EquipmentsResponse> {
        return networkSource.getCryptoCurrencyEquipments().map { it }.toFlowable()
    }

    fun getStockEquipments(): Flowable<EquipmentsResponse> {
        return networkSource.getStockEquipments().map { it }.toFlowable()
    }

    fun getEquipment(code: String): Single<EquipmentResponse> {
        return networkSource.getEquipment(code)
    }

    fun getComments(code: String): Single<List<CommentResponse>> {
        return networkSource.getEquipmentComments(code)
    }
}