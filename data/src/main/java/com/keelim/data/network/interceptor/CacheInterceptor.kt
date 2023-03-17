/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.data.network.interceptor

import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptor @Inject constructor() : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val useCache = request.header(HEADER_USE_CACHE_PREFIX) != null
        return chain
            .proceed(request).apply {
                if (useCache) {
                    newBuilder()
                        .header(HEADER_CACHE_CONTROL, HEADER_CACHE_MAX_AGE)
                        .removeHeader(HEADER_USE_CACHE_PREFIX)
                        .build()
                }
            }
    }

    companion object {
        const val HEADER_CACHE_CONTROL = "Cache-Control"
        const val HEADER_CACHE_MAX_AGE = "public, max-age=${3 * 60}" // 3분
        const val HEADER_USE_CACHE_PREFIX = "Use-Cache"
        const val HEADER_USE_CACHE = "$HEADER_USE_CACHE_PREFIX: "
    }
}
