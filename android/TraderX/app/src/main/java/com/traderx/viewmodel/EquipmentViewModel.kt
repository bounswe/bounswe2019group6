package com.traderx.viewmodel

import com.traderx.api.RequestService
import com.traderx.api.request.AlertRequest
import com.traderx.api.request.CommentRequest
import com.traderx.api.response.*
import com.traderx.db.EquipmentDao
import com.traderx.type.PredictionType
import com.traderx.type.VoteType
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class EquipmentViewModel(
    private val dataSource: EquipmentDao,
    private val networkSource: RequestService
) : BaseViewModel(), CommentableViewModel {

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

    override fun getComments(code: Any): Single<ArrayList<CommentResponse>> {
        return networkSource.getEquipmentComments(code as String)
    }

    fun getAlerts(): Single<ArrayList<AlertResponse>> {
        return networkSource.getAlerts()
    }

    fun createAlert(alert: AlertRequest): Completable {
        return networkSource.createAlert(alert)
    }

    override fun createComment(code: Any, comment: String): Single<CommentResponse> {
        return networkSource.createComment(code as String, CommentRequest(comment))
    }

    override fun editComment(id: Int, message: String): Completable {
        return networkSource.editComment(id, CommentRequest(message))
    }

    override fun voteComment(id: Int, voteType: VoteType): Completable {
        return networkSource.voteComment(id, voteType.request)
    }

    override fun revokeComment(id: Int): Completable {
        return networkSource.revokeComment(id)
    }

    fun deleteAlert(id: Int): Completable {
        return networkSource.deleteAlert(id)
    }

    override fun deleteComment(id: Int): Completable {
        return networkSource.deleteComment(id)
    }
    
    fun createPrediction(code: String, type: PredictionType): Completable {
        return networkSource.createPrediction(code, type.value)
    }

    fun getPredictions(username: String): Single<PredictionResponse> {
        return networkSource.getPredictions(username)
    }
}