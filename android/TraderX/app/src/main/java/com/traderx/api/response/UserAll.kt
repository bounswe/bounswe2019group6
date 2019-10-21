package com.traderx.api.response

import com.google.gson.annotations.SerializedName

data class UserAll(
    @SerializedName("username")
    val username: String
)