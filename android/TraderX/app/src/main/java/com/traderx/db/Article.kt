package com.traderx.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "article"
)
data class Article(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "header") val header: String,
    @ColumnInfo(name = "body") val body: String,
    @ColumnInfo(name = "tags") val tags: List<String>,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "createdAt") val createdAt: String
)

