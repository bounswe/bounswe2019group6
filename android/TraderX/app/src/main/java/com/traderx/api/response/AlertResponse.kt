package com.traderx.api.response

import com.traderx.type.AlertType
import com.traderx.type.TransactionType

data class AlertResponse(
    val id: Int,
    val alertType: AlertType,
    val amount: Double,
    val limitValue: Double,
    val orderType: TransactionType,
    val username: String,
    val createdAt: Long,
    val code: String
)