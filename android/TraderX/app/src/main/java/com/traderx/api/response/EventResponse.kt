package com.traderx.api.response

import com.google.gson.annotations.SerializedName

data class EventResponse(
    @SerializedName("Event") val event: String,
    @SerializedName("Country") val country: String,
    @SerializedName("Importance") val importance: Int,
    @SerializedName("Date") val date: String
)