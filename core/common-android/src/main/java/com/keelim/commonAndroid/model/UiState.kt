package com.keelim.commonAndroid.model

import com.keelim.commonAndroid.core.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface SealedUiState<out T> : State {
    data object Loading : SealedUiState<Nothing>
    data class Error(val throwable: Throwable?) : SealedUiState<Nothing>
    data class Success<T>(val value: T) : SealedUiState<T>

    fun getOrDefault(defaultValue: @UnsafeVariance T): T {
        return if (this is Success) {
            value
        } else {
            defaultValue
        }
    }

    companion object {
        fun <T> success(value: T): SealedUiState<T> = Success(value)
        fun <T> loading(): SealedUiState<T> = Loading
        fun <T> error(throwable: Throwable): SealedUiState<T> = Error(throwable)
    }
}

fun <T> Flow<T>.asSealedUiState(): Flow<SealedUiState<T>> {
    return map { SealedUiState.success(it) }
        .onStart { emit(SealedUiState.loading()) }
        .catch { emit(SealedUiState.error(it)) }
}
