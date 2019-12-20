package com.traderx.api.response

import com.traderx.type.VoteType

data class CommentResponse(
    val author: String,
    var comment: String,
    val equipment: String,
    val id: Int,
    val lastModifiedTime: Long,
    var likes: Int,
    var dislikes: Int,
    var status: VoteType
)