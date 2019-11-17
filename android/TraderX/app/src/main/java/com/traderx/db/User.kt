package com.traderx.db

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.traderx.R
import com.traderx.enum.FollowingStatus
import com.traderx.enum.Role

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
    @ColumnInfo(name = "roles") val roles: List<String>,
    @ColumnInfo(name = "isPrivate") val isPrivate: Boolean,
    @ColumnInfo(name = "followersCount") val followersCount: Int,
    @ColumnInfo(name = "followingsCount") val followingsCount: Int,
    @ColumnInfo(name = "articlesCount") val articlesCount: Int,
    @ColumnInfo(name = "commentsCount") val commentsCount: Int,
    @ColumnInfo(name = "followingStatus") val followingStatus: String?,
    @ColumnInfo(name = "iban") val iban: String?
) {
    val role: String get() = roles[0]

    fun localizedRole(context: Context): String = when (role) {
        Role.ROLE_ADMIN.value -> context.getString(R.string.role_admin)
        Role.ROLE_TRADER.value -> context.getString(R.string.role_trader)
        else -> context.getString(R.string.role_basic)
    }

    fun localizedIsPrivate(context: Context): String =
        if (isPrivate) context.getString(R.string.sprivate) else context.getString(R.string.spublic)

    fun localizedFollowingStatus(context: Context): String = when(followingStatus) {
        FollowingStatus.FOLLOWING.value -> context.getString(R.string.following)
        FollowingStatus.NOT_FOLLOWING.value -> context.getString(R.string.follow)
        FollowingStatus.PENDING.value ->context.getString(R.string.pending)
        else -> ""
    }
}