package com.traderx.api.request

data class ArticleRequest(
    val header: String,
    val tags: List<String>,
    val body: String
)