package com.traderx.api.request

data class AlertRequest (
    val code: String,
    val limit: Double,
    val amount: Double,
    val alertType: String,
    val orderType: String
)