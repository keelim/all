package com.keelim.core.network.di

import java.io.IOException

data class RetryPolicy(
    val maxRetries: Int,
    val baseDelayMillis: Long,
    val retryOnServerErrors: Boolean,
    val retryOnConnectionErrors: Boolean,
) {
    fun shouldRetryOn(throwable: Throwable): Boolean {
        return retryOnConnectionErrors && throwable is IOException
    }
}

data class TimeoutPolicy(
    val connectTimeoutMillis: Long,
    val readTimeoutMillis: Long,
    val writeTimeoutMillis: Long,
    val requestTimeoutMillis: Long,
)
