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
