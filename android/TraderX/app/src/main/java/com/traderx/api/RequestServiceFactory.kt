package com.traderx.api

import android.util.Log
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

var i = 0

@Module
class RequestServiceFactory {
    @Singleton
    @Provides
    fun provideRequestService(): RequestService {
        //Debug purpose only
        Log.d("provideRequestService", (++i).toString())

        return Retrofit.Builder()
            .baseUrl("http://192.168.1.5:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(RequestService::class.java)
    }
}