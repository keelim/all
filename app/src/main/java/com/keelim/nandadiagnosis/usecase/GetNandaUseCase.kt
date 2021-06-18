package com.keelim.nandadiagnosis.usecase

import com.keelim.nandadiagnosis.data.db.entity.NandaEntity2
import com.keelim.nandadiagnosis.data.repository.IORepository
import javax.inject.Inject

class GetNandaUseCase @Inject constructor(
    private val ioRepository: IORepository
) {

    suspend operator fun invoke(uid: Long): NandaEntity2? {
        return ioRepository.getNandaItem(uid)
    }
}