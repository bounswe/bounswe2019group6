package com.traderx.db

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun listToString(keywords: List<String>?): String? {
        return keywords?.joinToString(",")
    }

    @TypeConverter
    fun stringToList(keywords: String?): List<String>? {
        return keywords?.split(",")
    }

    @TypeConverter
    fun predictionStatsToString(stats: User.PredictionStatus?): String? {
        return stats?.let {
            it.totalPredictions.toString() + "," + it.notEvaluated.toString() + "," +
                    it.success.toString() + "," + it.fail.toString() + "," + it.successPercentage
        }
    }

    @TypeConverter
    fun stringToPredictionStats(stats: String?): User.PredictionStatus? {
        return stats?.let {
            val split = it.split(",")

            if(split.size < 5){
                null
            }
            User.PredictionStatus(
                split[0].toInt(),
                split[1].toInt(),
                split[2].toInt(),
                split[3].toInt(),
                split[4].toFloat()
            )
        }
    }
}