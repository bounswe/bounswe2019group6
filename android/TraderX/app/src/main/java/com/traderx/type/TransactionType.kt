package com.traderx.type

enum class TransactionType(val request: String, val response: String) {
    BUY("buy", "BUY"),
    SELL("sell", "SELL"),
    NOTIFY("notify","NOTIFY")
}