package com.traderx.api

import android.content.Context
import com.traderx.AppConfig
import com.traderx.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class ApiService {

    companion object {

        private var INSTANCE: RequestService? = null
        private var CONTEXT_INSTANCE: RequestService? = null

        fun refreshInstance() {
            CONTEXT_INSTANCE = null
            INSTANCE = null
        }

        fun getInstance(context: Context? = null): RequestService {
            return if (context != null) {
                CONTEXT_INSTANCE ?: provideRequestService(context).also { CONTEXT_INSTANCE = it }
            } else {
                INSTANCE ?: provideRequestService(context).also { INSTANCE = it }
            }
        }

        private fun provideRequestService(context: Context?): RequestService {
            val clientBuilder = OkHttpClient.Builder()

            if (context != null) {
                clientBuilder.addInterceptor(RetrofitInterceptor(context))
            }

            return Retrofit.Builder()
                .baseUrl(AppConfig.API_HOST)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .build()
                .create(RequestService::class.java)
        }
    }
}

class RetrofitInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val token =
            context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
                .getString("token", "")

        val headers =
            request.headers().newBuilder().add("Authorization", "Bearer $token").build()

        request = request.newBuilder().headers(headers).build()

        return chain.proceed(request)
    }
}