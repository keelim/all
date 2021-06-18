package com.keelim.nandadiagnosis.usecase

import com.keelim.nandadiagnosis.data.db.entity.NandaEntity2
import com.keelim.nandadiagnosis.data.repository.IORepository
import javax.inject.Inject

class GetNandaListUseCase @Inject constructor(
    private val ioRepository: IORepository
) {

    suspend operator fun invoke(): List<NandaEntity2> {
        return ioRepository.getLocalNandaList()
    }
}