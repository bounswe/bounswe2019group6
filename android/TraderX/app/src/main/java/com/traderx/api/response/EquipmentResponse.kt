package com.traderx.api.response

import com.traderx.db.Equipment

data class EquipmentResponse(
    val base: String,
    val equipments: List<Equipment>
)