package com.keelim.player

import com.keelim.nandadiagnosis.data.model.Video

sealed class PlayState {
    object UnInitialized : PlayState()
    object Loading : PlayState()
    data class Success(
        val data: List<Video>
    ) : PlayState()

    data class Error(
        val message: String,
    ) : PlayState()
}
