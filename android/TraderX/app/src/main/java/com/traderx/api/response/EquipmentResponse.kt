package com.traderx.api.response

import android.content.Context
import com.traderx.R
import com.traderx.enum.EquipmentType

data class EquipmentResponse(
    val equipment: Equipment,
    val historicalValues: List<History>
) {
    data class Equipment(
        val code: String,
        val currentStock: Double,
        val currentValue: Double,
        val equipmentType: EquipmentType,
        val name: String,
        val predictionRate: Double,
        val timeZone: String
    ) {
        public fun localizedType(context: Context): String {
            val stringId = when (equipmentType) {
                EquipmentType.CURRENCY -> R.string.currencies
                EquipmentType.STOCK -> R.string.stocks
                else -> R.string.crypto_currencies
            }

            return context.getString(stringId)
        }
    }

    data class History(
        val close: Double,
        val high: Double,
        val low: Double,
        val open: Double
    )
}