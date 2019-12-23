package com.traderx.api.request

import com.traderx.type.AnnotationType

data class AnnotationRequest(
    val articleId: Int,
    val content: String,
    val targetType: String,
    val bodyType: String,
    val posStart: Int,
    val posEnd: Int
)