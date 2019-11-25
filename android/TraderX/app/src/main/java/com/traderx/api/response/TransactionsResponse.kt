package com.traderx.api.response

import com.google.gson.annotations.SerializedName

data class TransactionsResponse(
    val equipment: String,
    val transactionType: String,
    val amount: Double,
    val createdAt: String,
    @SerializedName("user") val username: String
)