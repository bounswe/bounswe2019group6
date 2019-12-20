package com.traderx.type

enum class VoteType(val value: String, val request: String) {
    DISLIKED("DISLIKED", "down"),
    LIKED("LIKED", "up"),
    NOT_COMMENTED("NOT_COMMENTED", "")
}