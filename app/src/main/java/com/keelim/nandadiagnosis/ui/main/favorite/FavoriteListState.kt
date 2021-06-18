package com.keelim.nandadiagnosis.ui.main.favorite

import com.keelim.nandadiagnosis.data.db.entity.NandaEntity


sealed class FavoriteListState {
    object UnInitialized : FavoriteListState()

    object Loading : FavoriteListState()

    data class Success(
        val favoriteList: List<NandaEntity>
    ) : FavoriteListState()

    object Error : FavoriteListState()
}
