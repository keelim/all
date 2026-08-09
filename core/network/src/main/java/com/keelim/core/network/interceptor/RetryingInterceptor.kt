package com.keelim.core.network.interceptor

import com.keelim.core.network.di.RetryPolicy
import okhttp3.Interceptor
import okhttp3.Response
import jakarta.inject.Inject
import java.io.IOException

class RetryingInterceptor
@Inject
constructor(
    private val retryPolicy: RetryPolicy,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var attempt = 0
        var lastException: IOException? = null

        while (attempt <= retryPolicy.maxRetries) {
            try {
                val response = chain.proceed(request)
                if (shouldRetryResponse(response = response, attempt = attempt)) {
                    response.close()
                    attempt++
                    continue
                }
                return response
            } catch (e: IOException) {
                if (shouldRetryException(exception = e, attempt = attempt).not()) {
                    throw e
                }
                lastException = e
                attempt++
            }
        }

        throw lastException ?: IOException("Request failed after ${retryPolicy.maxRetries} retries")
    }

    private fun shouldRetryResponse(
        response: Response,
        attempt: Int,
    ): Boolean {
        return retryPolicy.retryOnServerErrors &&
            response.isSuccessful.not() &&
            attempt < retryPolicy.maxRetries
    }

    private fun shouldRetryException(
        exception: IOException,
        attempt: Int,
    ): Boolean {
        return retryPolicy.shouldRetryOn(exception) && attempt < retryPolicy.maxRetries
    }
}
