package com.keelim.comssa.usecase

import com.keelim.comssa.data.db.entity.Search
import com.keelim.comssa.data.repository.IoRepository
import javax.inject.Inject

class UpdateFavoriteUseCase @Inject constructor(
    private val ioRepository: IoRepository,
) {

    suspend operator fun  invoke(favorite:Int, id:Int) {
        ioRepository.updateFavorite(favorite, id)
    }
}