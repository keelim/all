package com.keelim.comssa.ui.favorite

import com.keelim.comssa.data.db.entity.Search

sealed class FavoriteState {
    object UnInitialized : FavoriteState()
    data class Success(
        val data: List<Search>
    ) : FavoriteState()

    object Loading : FavoriteState()
    object Error : FavoriteState()
}
