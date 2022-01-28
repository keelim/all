package com.keelim.cnubus.feature.map.ui.map3.detail

import com.keelim.cnubus.data.db.entity.Comment

sealed class DetailState {
    object UnInitialized : DetailState()
    object Loading : DetailState()
    data class Success(
        val data: List<Comment>
    ) : DetailState()

    data class Error(
        val message: String
    ) : DetailState()
}
