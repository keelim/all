package com.keelim.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class RetryingInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return process(chain, attempt = 1)
    }

    private fun process(chain: Interceptor.Chain, attempt: Int): Response {
        var response: Response? = null
        try {
            val request = chain.request()
            response = chain.proceed(request)
            if (attempt < TRY_CNT && !response.isSuccessful) {
                return delayedAttempt(chain, response, attempt)
            }
            return response
        } catch (e: Exception) {
            if (attempt < TRY_CNT && networkRetryCheck(e)) {
                return delayedAttempt(chain, response, attempt)
            }
            throw e
        }
    }

    private fun delayedAttempt(
        chain: Interceptor.Chain,
        response: Response?,
        attempt: Int,
    ): Response {
        response?.body?.close()
        Thread.sleep(BASE_INTERVAL * attempt)
        return process(chain, attempt = attempt + 1)
    }

    private fun networkRetryCheck(throwable: Throwable): Boolean {
        return true
    }

    companion object {
        const val TRY_CNT = 3
        const val BASE_INTERVAL = 2000L
    }
}
