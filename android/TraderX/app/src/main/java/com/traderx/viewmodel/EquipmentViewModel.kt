package com.traderx.viewmodel

import com.traderx.api.RequestService
import com.traderx.api.request.AlertRequest
import com.traderx.api.request.CommentRequest
import com.traderx.api.response.AlertResponse
import com.traderx.api.response.CommentResponse
import com.traderx.api.response.EquipmentResponse
import com.traderx.api.response.EquipmentsResponse
import com.traderx.db.EquipmentDao
import io.reactivex.Completable
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

    fun getComments(code: String): Single<ArrayList<CommentResponse>> {
        return networkSource.getEquipmentComments(code)
    }

    fun getAlerts(): Single<ArrayList<AlertResponse>> {
        return networkSource.getAlerts()
    }

    fun createAlert(alert: AlertRequest): Completable {
        return networkSource.createAlert(alert)
    }

    fun createComment(code: String, comment: String): Completable {
        return networkSource.createComment(code, CommentRequest(comment))
    }

    fun deleteAlert(id: Int): Completable {
        return networkSource.deleteAlert(id)
    }

    fun deleteComment(id: Int): Completable {
        return networkSource.deleteComment(id)
    }
}