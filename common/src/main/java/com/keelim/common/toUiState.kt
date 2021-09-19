package com.keelim.common

import com.keelim.compose.ui.UiState

fun <T> Result<T>.toUiState(): UiState<T> {
    return when (this) {
        is Result.Error -> UiState(exception = this.exception)
        Result.Loading -> UiState.loading()
        is Result.Success -> UiState.success(value = this.data)
    }
}