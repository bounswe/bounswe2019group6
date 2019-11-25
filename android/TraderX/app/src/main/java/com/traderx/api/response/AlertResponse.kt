package com.traderx.api.response

import com.traderx.enum.AlertType
import com.traderx.enum.TransactionType

data class AlertResponse(
    val alertType: AlertType,
    val amount: Double,
    val limitValue: Double,
    val transactionType: TransactionType,
    val username: String,
    val createdAt: Long,
    val code: String
)