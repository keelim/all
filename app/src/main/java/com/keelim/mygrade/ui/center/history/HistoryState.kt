package com.keelim.mygrade.ui.center.history

import com.keelim.data.db.entity.History


sealed class HistoryState {
    object UnInitialized : HistoryState()
    object Loading : HistoryState()
    data class Error(
        val message: String
    ) : HistoryState()
    data class Success(
        val data: List<History>
    ) : HistoryState()
}
