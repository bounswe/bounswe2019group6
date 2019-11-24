package com.traderx.api.response

data class TransactionsResponse(
    val equipment: String,
    val transactionType: String,
    val amount: Double,
    val date: String
)