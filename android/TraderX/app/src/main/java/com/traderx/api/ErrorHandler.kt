package com.traderx.api

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.room.EmptyResultSetException
import com.google.gson.GsonBuilder
import com.traderx.R
import com.traderx.api.response.ErrorResponse
import com.traderx.ui.auth.login.LoginActivity
import retrofit2.HttpException
import java.net.ConnectException

class ErrorHandler<T> {

    companion object {

        private const val TAG = "ErrorHandler"

        fun handleError(error: Throwable, context: Context): Boolean {

            if (error is HttpException) {
                Log.e(TAG, error.code().toString() + " " + error.message())

                if (error.code() == 403 || error.code() == 401) {
                    context.startActivity(Intent(context, LoginActivity::class.java))

                    return true
                }
            } else if (error is ConnectException) {
                //Show a connection error
                AlertDialog.Builder(context)
                    .setMessage(context.getString(R.string.connection_error))
                    .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->
                    }).create().show()

                return true
            } else {
                Log.e(TAG, error.javaClass.name)
                Log.e(TAG, error.message)
            }

            return false
        }

        fun handleUserViewError(error: Throwable, context: Context): Boolean {

            if (error is EmptyResultSetException) {
                context.startActivity(Intent(context, LoginActivity::class.java))

                return true
            }

            return false
        }


        fun parseErrorMessage(serializedMessage: String): ErrorResponse {
            val gson = GsonBuilder().create()

            return gson.fromJson<ErrorResponse>(serializedMessage, ErrorResponse::class.java)
        }
    }

}