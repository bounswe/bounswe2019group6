package com.traderx.api

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.traderx.R
import com.traderx.activity.LoginActivity
import com.traderx.api.response.ErrorResponse
import retrofit2.HttpException
import java.net.ConnectException

class ResponseHandler<T> {

    companion object {

        private const val TAG = "ResponseHandler"

        fun handleError(error: Throwable, activity: Activity): Boolean {
            var errorHandled = false

            if (error is HttpException) {
                Log.e(TAG, error.code().toString() + " " + error.message())

                if (error.code() == 403 || error.code() == 401) {
                    activity.startActivity(Intent(activity, LoginActivity::class.java))

                    errorHandled = true
                }
            } else if (error is ConnectException) {
                //Show a connection error
                activity.runOnUiThread {
                    AlertDialog.Builder(activity)
                        .setMessage(activity.getString(R.string.connection_error))
                        .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->
                        }).create().show()
                }

                errorHandled = true
            } else {
                Log.e(TAG, error.javaClass.name)
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