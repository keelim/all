package com.keelim.comssa.usecase

import com.keelim.comssa.data.db.entity.Search
import com.keelim.comssa.data.repository.IoRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val ioRepository: IoRepository,
) {

    suspend operator fun  invoke(keyword:String?): List<Search>{
        return ioRepository.getSearch(keyword.orEmpty())
    }
}