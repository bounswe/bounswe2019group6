package com.traderx.api.response

data class PortfolioEquipment(
    val code: String,
    val data: EquipmentsResponse.Equipment.Data,
    var enabled : Boolean
)