package com.keelim.nandadiagnosis.domain.video

import com.keelim.nandadiagnosis.data.dto.VideoDto
import com.keelim.nandadiagnosis.data.repository.io.IORepository

class VideoUseCase(
    private val ioRepository: IORepository
) {
    suspend fun getVideoList(): VideoDto{
        return ioRepository.getVideoList()
    }
}