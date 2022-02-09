package com.keelim.cnubus.di

import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptor @Inject constructor(): Interceptor {
    private val HEADER_CACHE_CONTROL = "Cache-Control"
    private val HEADER_CACHE_MAX_AGE = "public, max-age=${3 * 60}" // 3분
    private val HEADER_USE_CACHE_PREFIX = "Use-Cache"
    private val HEADER_USE_CACHE = "$HEADER_USE_CACHE_PREFIX: "

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val useCache = request.header(HEADER_USE_CACHE_PREFIX) != null
        return chain.proceed(request).apply {
            if (useCache) {
                newBuilder()
                    .header(HEADER_CACHE_CONTROL, HEADER_CACHE_MAX_AGE)
                    .removeHeader(HEADER_USE_CACHE_PREFIX)
                    .build()
            }
        }
    }
}