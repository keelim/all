package com.keelim.nandadiagnosis.usecase

import com.keelim.nandadiagnosis.data.db.entity.NandaEntity
import com.keelim.nandadiagnosis.data.repository.IORepository
import javax.inject.Inject

class GetFavoriteListUseCase @Inject constructor(
    private val ioRepository: IORepository,
) {

    suspend operator fun invoke(): List<NandaEntity>{
        return ioRepository.getFavoriteList()
    }
}