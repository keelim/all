package com.keelim.cnubus.data.model.state

sealed class UiState<out R> {
    object UnInitialized : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<out T>(
        val data: T,
    ) : UiState<T>()
    data class Error(
        val exception: Exception
    ): UiState<Nothing>()
}