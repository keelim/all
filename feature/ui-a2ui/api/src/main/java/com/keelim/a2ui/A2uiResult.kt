package com.keelim.a2ui

sealed interface A2uiResult {
    data object Accepted : A2uiResult
    data object Rejected : A2uiResult
}
