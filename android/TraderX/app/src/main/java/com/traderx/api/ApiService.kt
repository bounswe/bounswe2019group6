package com.traderx.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class ApiService {

    companion object {

        private var INSTANCE: RequestService? = null

        fun refreshInstance() {
            INSTANCE = null
        }

        fun getInstance(): RequestService {
            return INSTANCE ?: provideRequestService().also { INSTANCE = it }
        }

        private fun provideRequestService(): RequestService {
            val clientBuilder = OkHttpClient.Builder()

            clientBuilder.addInterceptor(RetrofitInterceptor())

            return Retrofit.Builder()
                .baseUrl(ApiUri.API_URI)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .build()
                .create(RequestService::class.java)
        }
    }
}


class ApiUri {
    companion object {
        const val API_URI = "http://192.168.1.5:8080"
        const val USER_URI = API_URI + "/users"
        const val USER_SIGNIN = USER_URI + "/signin"
        const val USER_SINGUP = USER_URI + "/signup"
        const val USER_INFO = USER_URI + "/me"
    }
}

class RetrofitInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        //TODO Get the token from where it is stored
        val token = ""

        val headers = request.headers().newBuilder().add("Authorization", "Bearer $token").build()

        request = request.newBuilder().headers(headers).build()

        return chain.proceed(request)
    }
}