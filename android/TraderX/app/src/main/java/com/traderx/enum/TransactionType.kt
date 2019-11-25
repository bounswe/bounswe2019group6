package com.traderx.enum

enum class TransactionType(val request: String, val response: String) {
    BUY("buy", "BUY"),
    SELL("sell", "SELL")
}