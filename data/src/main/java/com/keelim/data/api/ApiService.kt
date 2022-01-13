

package com.keelim.data.api;

import com.keelim.data.BuildConfig
import com.keelim.data.api.response.ResponseNotification
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun getNotification(@Url rul:String = BuildConfig.VERSION): Response<ResponseNotification>
}