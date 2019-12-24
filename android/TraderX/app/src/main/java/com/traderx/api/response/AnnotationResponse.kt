package com.traderx.api.response

import com.google.gson.annotations.SerializedName

data class AnnotationResponse(
    @SerializedName("@context") val context: String,
    val id: Int,
    val type: String,
    val target: Target,
    val body: Body,
    val modified: String,
    val created: String,
    val creator: String
) {
    data class Target(
        val selector: Selector?,
        val id: String,
        val type: String
    ) {
        data class Selector(
            val start: Int,
            val end: Int,
            val type: String
        )
    }

    data class Body(
        val type: String,
        val value: String
    )

}