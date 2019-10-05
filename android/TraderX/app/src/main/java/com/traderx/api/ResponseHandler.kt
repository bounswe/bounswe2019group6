package com.traderx.api

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.annotation.NonNull
import com.traderx.activity.LoginActivity

class ResponseHandler<T> {

    companion object {

        private const val TAG = "ResponseHandler"

        fun handleError(error: Throwable, activity: Activity) {
            Log.d(TAG, error.message)


            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
        }
    }
}