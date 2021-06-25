package com.keelim.comssa.ui.main

import com.keelim.comssa.data.db.entity.Search

sealed class MainListState {
    object UnInitialized : MainListState()

    object Loading: MainListState()

    data class Success(
        val searchList: List<Search>
    ) : MainListState()

    object Error:MainListState()
}
