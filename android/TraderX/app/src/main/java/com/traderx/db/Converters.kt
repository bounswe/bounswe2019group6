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
}