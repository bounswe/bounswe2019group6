package com.traderx.api

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.traderx.activity.LoginActivity

class ResponseHandler<T> {

    fun handleError(error: Throwable, activity: Activity) {
        Log.d("Handle Error", error.message)
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivity(intent)
    }
}