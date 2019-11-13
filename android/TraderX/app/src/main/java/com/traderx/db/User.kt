package com.traderx.db

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.traderx.R

@Entity(
    tableName = "user"
)
data class User(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int = 1,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "latitude") val latitude: Float,
    @ColumnInfo(name = "longitude") val longitude: Float,
    @ColumnInfo(name = "role") val roles: List<String>,
    @ColumnInfo(name = "isPrivate") val isPrivate: Boolean,
    @ColumnInfo(name = "followersCount") val followerCount: Int,
    @ColumnInfo(name = "followingsCount") val followingCount: Int,
    @ColumnInfo(name = "articlesCount") val articlesCount: Int,
    @ColumnInfo(name = "commentsCount") val commentsCount: Int,
    @ColumnInfo(name = "iban") val iban: String?
) {
    val role: String get() = roles[0]
    fun localizedRole(context: Context): String = when (role) {
        "ROLE_ADMIN" -> context.getString(R.string.role_admin)
        "ROLE_TRADER" -> context.getString(R.string.role_trader)
        else -> context.getString(R.string.role_basic)
    }
}