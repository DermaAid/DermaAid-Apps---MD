package com.example.dermaaid.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val retrofit: Retrofit by lazy {
        // Create a logging interceptor
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Log request and response bodies
        }

        // Build OkHttpClient with the interceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        // Build Retrofit with the OkHttpClient
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL) // Replace with your actual BASE_URL
            .addConverterFactory(GsonConverterFactory.create())
            .client(client) // Attach the OkHttpClient
            .build()
    }

    val newsApiService: NewsApiService by lazy {
        retrofit.create(NewsApiService::class.java)
    }
}
