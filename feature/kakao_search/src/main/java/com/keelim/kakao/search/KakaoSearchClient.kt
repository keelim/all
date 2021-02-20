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
package com.keelim.kakao.search

import com.keelim.kakao.search.enums.KakaoSearchSortType
import com.keelim.kakao.search.model.BaseKakaoResponse
import com.keelim.kakao.search.model.KakaoImageResponse
import com.keelim.kakao.search.model.KakaoWebResponse
import com.keelim.kakao.search.network.KakaoSearchApi
import com.keelim.kakao.search.network.NetworkManager
import retrofit2.create

fun kakaoSearchClient(
  apiKey: String,
) = lazy {
  val keyHeaderValue = "KakaoAK $apiKey"
  KakaoSearchClient(keyHeaderValue)
}

class KakaoSearchClient(
  private val kakaoRestApiKey: String,
) {

  private val kakaoSearchApi by lazy {
    NetworkManager.retrofit.create<KakaoSearchApi>()
  }

  suspend fun searchWeb(
    query: String,
    sort: KakaoSearchSortType = KakaoSearchSortType.ACCURACY,
    page: Int = 1,
    size: Int = 10,
  ): BaseKakaoResponse<KakaoWebResponse> {
    return kakaoSearchApi.searchWeb(kakaoRestApiKey, query, sort, page, size)
  }

  suspend fun searchImage(
    query: String,
    sort: KakaoSearchSortType = KakaoSearchSortType.ACCURACY,
    page: Int = 1,
    size: Int = 80,
  ): BaseKakaoResponse<KakaoImageResponse> {
    return kakaoSearchApi.searchImage(kakaoRestApiKey, query, sort, page, size)
  }
}
