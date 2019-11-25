package com.traderx.api.response

data class EquipmentsResponse(
    val base: String,
    val equipments: List<Equipment>
) {
    data class Equipment(
        val code: String,
        val data: Data
    ) {
        data class Data(
            val currentStock: Double,
            val currentValue: Double
        )
    }
}