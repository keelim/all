package com.keelim.comssa.usecase

import com.keelim.comssa.data.model.Data
import com.keelim.comssa.data.repository.DataRepository
import javax.inject.Inject

class GetAllDatasUseCase @Inject constructor(
    private val dataRepository: DataRepository,
) {
    suspend operator fun invoke():List<Data> = dataRepository.getAllDatas()
}