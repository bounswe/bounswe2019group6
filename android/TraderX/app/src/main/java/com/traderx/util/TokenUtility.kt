package com.traderx.util

import android.content.Context
import com.traderx.BuildConfig
import com.traderx.api.ApiService

class TokenUtility {
    companion object {
        fun storeToken(token: String, context: Context) {
            val editor = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE).edit()

            editor.putString("token", token)

            editor.apply()

            ApiService.refreshInstance()
        }

        fun clearToken(context: Context) {
            storeToken("", context)
        }
    }
}