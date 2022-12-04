package com.keelim.data.api

import com.keelim.data.response.ReleaseNote
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.keelim.data.BuildConfig
import com.keelim.data.response.ResponseNotification
import javax.inject.Inject
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Url

class ApiRequestFactory @Inject constructor(
) {
    private val baseUrl = "https://api.github.com/"

    val retrofit: ApiService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaTypeOrNull()!!))
        .client(
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        this.level = HttpLoggingInterceptor.Level.BODY
                    }
                )
                .build()
        )
        .build()
        .create()
}

class NotionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        return chain.proceed(
            request.newBuilder()
                .addHeader("Accept", "application/json")
                .build()
        )
    }
}

interface ApiService {
    @GET
    suspend fun getNotification(@Url rul: String = BuildConfig.VERSION): Response<ResponseNotification>

    @GET
    suspend fun getReleaseNotes(@Url rul: String = BuildConfig.BASE_NOTION): Response<ReleaseNote>
}
