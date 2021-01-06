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
