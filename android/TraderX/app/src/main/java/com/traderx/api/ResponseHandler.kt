package com.traderx.api

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.traderx.activity.LoginActivity
import com.traderx.api.response.ErrorResponse
import retrofit2.HttpException

class ResponseHandler<T> {

    companion object {

        private const val TAG = "ResponseHandler"

        fun handleError(error: Throwable, activity: Activity): Boolean {
            var errorHandled = false

            if (error is HttpException) {
                Log.e(TAG, error.code().toString() + " " + error.message())

                if (error.code() == 403) {
                    val intent = Intent(activity, LoginActivity::class.java)
                    activity.startActivity(intent)

                    errorHandled = true
                }
            } else {
                Log.e(TAG + "unknown", error.message)
            }

            return errorHandled
        }

        fun parseErrorMessage(serializedMessage: String): ErrorResponse {
            val gson = GsonBuilder().create()

            return gson.fromJson<ErrorResponse>(serializedMessage, ErrorResponse::class.java)
        }
    }
}