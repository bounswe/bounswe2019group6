package com.traderx.api.response

import com.google.gson.annotations.SerializedName

data class SuccessResponse(
    @SerializedName("message")
    val message: String
)