package com.traderx.viewmodel

import com.traderx.api.RequestService
import com.traderx.db.Equipment
import com.traderx.db.EquipmentDao
import io.reactivex.Flowable
import io.reactivex.Single

class EquipmentViewModel(
    private val dataSource: EquipmentDao,
    private val networkSource: RequestService
) : BaseViewModel() {

    fun getCurrencyEquipments(): Flowable<List<String>> {
        return networkSource.getCurrencyEquipments().map { it.equipments }.toFlowable()
    }

    fun getCryptoCurrencyEquipments(): Flowable<List<String>> {
        return networkSource.getCryptoCurrencyEquipments().map { it.equipments }.toFlowable()
    }

    fun getStockEquipments(): Flowable<List<String>> {
        return networkSource.getStockEquipments().map { it.equipments }.toFlowable()
    }
}