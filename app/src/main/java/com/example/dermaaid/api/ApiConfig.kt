package com.example.dermaaid.api

import com.example.dermaaid.BuildConfig

object ApiConfig {
    const val BASE_URL: String = BuildConfig.NEWS_API_URL
    val API_KEY: String = BuildConfig.NEWS_API_KEY.ifEmpty { "INVALID_API_KEY" }
}
