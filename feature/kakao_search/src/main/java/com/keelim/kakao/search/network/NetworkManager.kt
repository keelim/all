/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
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
package com.keelim.kakao.search.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object NetworkManager {

  private const val KAKAO_HOST_URL = "https://dapi.kakao.com/"

  val retrofit: Retrofit by lazy {
    Retrofit.Builder()
      .baseUrl(KAKAO_HOST_URL)
      .client(client)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  private val client: OkHttpClient by lazy {
    OkHttpClient.Builder()
      .build()
  }
}
