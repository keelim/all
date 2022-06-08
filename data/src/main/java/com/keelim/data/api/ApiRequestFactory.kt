package com.keelim.data.api
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Inject
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create

class ApiRequestFactory @Inject constructor(
) {
    private val baseUrl = "https://sample.com"

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
