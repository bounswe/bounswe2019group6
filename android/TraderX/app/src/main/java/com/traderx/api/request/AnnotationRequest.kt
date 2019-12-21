package com.traderx.api.request

data class AnnotationRequest(
    val text: String,
    val start: Int,
    val end: Int
)