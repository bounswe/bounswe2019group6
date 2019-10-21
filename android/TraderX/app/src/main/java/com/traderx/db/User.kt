package com.traderx.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.ObjectInputStream

@Entity(
    tableName = "user",
    indices = [Index("id")]
)
data class User(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "latitude") val latitude: Float,
    @ColumnInfo(name = "longitude") val longitude: Float,
    @ColumnInfo(name = "role") val role: String,
    @ColumnInfo(name = "token") val token: String?
)