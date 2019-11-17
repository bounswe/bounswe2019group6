package com.traderx.util

import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


object Helper {
    fun <T> applySchedulers(): SingleTransformer<T, T> {
        return SingleTransformer{ single: Single<T> ->
            single.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
}