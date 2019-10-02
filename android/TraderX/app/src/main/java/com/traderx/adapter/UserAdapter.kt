package com.traderx.adapter

import com.traderx.api.response.LoginResponse
import com.traderx.api.response.UserResponse
import com.traderx.db.User

class UserAdapter {
    companion object {
        fun adapt(userResponse: UserResponse): User {
            return User(id = userResponse.id, name = userResponse.name, token = null)
        }

        fun adaptLoginResponse(loginResponse: LoginResponse): User {
            return User(id = loginResponse.id, name = loginResponse.name, token = loginResponse.token)
        }
     }
}