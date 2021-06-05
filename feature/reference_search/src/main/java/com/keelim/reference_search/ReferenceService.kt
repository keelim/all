package com.keelim.reference_search

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ReferenceService {

    @GET("v1/search/book.json")
    fun getReference(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientPw:String,
        @Query("query") keyword:String
    ) : Call<Reference>

}