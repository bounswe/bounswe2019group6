package com.traderx.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    protected fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
    }
}