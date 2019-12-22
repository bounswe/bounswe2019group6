package com.traderx.api.request

data class AnnotationRequest(
    val articleId: Int,
    val text: String,
    val start: Int,
    val end: Int
)