package com.traderx.api.response

import java.io.Serializable

data class CommentResponse(
    val author: String,
    val comment: String,
    val equipment: String,
    val id: Int,
    val lastModifiedTime: Long
) : Serializable