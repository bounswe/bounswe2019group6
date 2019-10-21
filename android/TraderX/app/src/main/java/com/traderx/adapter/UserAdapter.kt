package com.traderx.adapter

import com.traderx.api.response.TokenResponse
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
                token = null
            )
        }
    }
}