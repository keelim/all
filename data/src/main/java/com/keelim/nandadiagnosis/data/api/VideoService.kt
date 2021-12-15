package com.keelim.nandadiagnosis.data.api

import com.keelim.nandadiagnosis.data.dto.VideoDto
import retrofit2.Response
import retrofit2.http.GET

interface VideoService {
    @GET("/v3/cbb293eb-e8b2-4079-ba82-472d1c0419d1")
    fun listVideos(): Response<VideoDto>
}