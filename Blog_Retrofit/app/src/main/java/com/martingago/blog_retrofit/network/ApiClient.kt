package com.martingago.blog_retrofit.network

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private var retrofitWithAuth: Retrofit? = null
    private var retrofitWithoutAuth: Retrofit? = null

    private const val BASE_URL = "https://blogspring.onrender.com/api/v1/"

    // Retrofit con AuthInterceptor (para peticiones autenticadas)
    fun getInstanceWithAuth(context: Context): Retrofit {
        if (retrofitWithAuth == null) {
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(AuthInterceptor(context)) // Agrega el AuthInterceptor
                .build()

            retrofitWithAuth = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofitWithAuth!!
    }

    // Retrofit sin AuthInterceptor (para peticiones no autenticadas, como login)
    fun getInstanceWithoutAuth(): Retrofit {
        if (retrofitWithoutAuth == null) {
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

            retrofitWithoutAuth = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofitWithoutAuth!!
    }
}
