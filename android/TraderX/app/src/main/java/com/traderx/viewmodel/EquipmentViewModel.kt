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

    fun fetchEquipments(): Single<List<Equipment>> {
        return networkSource.getEquipments().map { it.equipments }
    }

    fun getEquipments(): Flowable<List<Equipment>> {
        return dataSource.getEquipments().flatMap { fetchEquipments().toFlowable() }
    }
}