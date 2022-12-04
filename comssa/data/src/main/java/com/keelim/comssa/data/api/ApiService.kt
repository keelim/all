package com.keelim.comssa.data.api

import retrofit2.http.GET

interface ApiService {
    @GET
    suspend fun getData(): String
}
