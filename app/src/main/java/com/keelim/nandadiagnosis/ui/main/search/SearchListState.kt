package com.keelim.nandadiagnosis.ui.main.search

import com.keelim.nandadiagnosis.data.db.entity.NandaEntity

sealed class SearchListState {
    object UnInitialized : SearchListState()

    object Loading : SearchListState()

    data class Success(
        val searchList: List<NandaEntity>
    ) : SearchListState()

    object Error : SearchListState()

    data class Searching(
        val searchList: List<NandaEntity>
    ) : SearchListState()
}