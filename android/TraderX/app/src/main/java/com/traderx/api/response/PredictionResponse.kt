package com.traderx.api.response

import com.traderx.type.PredictionType

data class PredictionResponse(
    val predictions: ArrayList<Prediction>,
    val username: String
) {
    data class Prediction(
        val equipmentCode: String,
        val isSucceeded: Boolean,
        val predictionDay: Long,
        val predictionId: Int,
        val predictionType: PredictionType
    )
}