package com.traderx.adapter

import com.traderx.R
import com.traderx.api.response.UserResponse
import com.traderx.db.User

class UserAdapter {
    companion object {
        fun adapt(userResponse: UserResponse): User {
            return User(
                id = userResponse.id,
                username = userResponse.username,
                email = userResponse.email,
                latitude = userResponse.latitude,
                longitude = userResponse.longitude,
                role = userResponse.roles[0] ?: "",
                isPrivate = userResponse.isPrivate,
                token = null
            )
        }

        fun adaptRole(role: String): Int {
            return when(role) {
                "ROLE_TRADER" -> R.string.role_trader
                "ROLE_ADMIN" -> R.string.role_admin
                else -> R.string.role_basic
            }
        }
    }
}