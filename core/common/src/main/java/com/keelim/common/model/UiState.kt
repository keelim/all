package com.keelim.common.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

data class DataUiState<T>(
    val isLoading: Boolean = false,
    val throwable: Throwable? = null,
    val value: T? = null,
) {
    val isError: Boolean
        get() = throwable != null

    fun getOrDefault(default: T): T {
        return value ?: default
    }

    companion object {
        fun <T> loading(): DataUiState<T> {
            return DataUiState(isLoading = true)
        }

        fun <T> success(value: T): DataUiState<T> {
            return DataUiState(value = value)
        }

        fun <T> error(throwable: Throwable): DataUiState<T> {
            return DataUiState(throwable = throwable)
        }
    }
}

sealed class SealedUiState<out T> {
    data class Loading<T>(val value: T?) : SealedUiState<T>()
    data class Error(val throwable: Throwable?) : SealedUiState<Nothing>()
    data class Success<T>(val value: T) : SealedUiState<T>()

    fun getOrDefault(defaultValue: @UnsafeVariance T): T {
        return if (this is Success) {
            value
        } else {
            defaultValue
        }
    }

    companion object {
        fun <T> success(value: T): SealedUiState<T> {
            return Success(value)
        }

        fun <T> loading(): SealedUiState<T> {
            return Loading(null)
        }

        fun <T> error(throwable: Throwable): SealedUiState<T> {
            return Error(throwable)
        }
    }
}

fun <T> Flow<T>.asDataUiState(): Flow<DataUiState<T>> {
    return map { DataUiState.success(it) }
        .onStart { emit(DataUiState.loading()) }
        .catch { emit(DataUiState.error(it)) }
}

fun <T> Flow<T>.asSealedUiState(): Flow<SealedUiState<T>> {
    return map { SealedUiState.success(it) }
        .onStart { emit(SealedUiState.loading()) }
        .catch { emit(SealedUiState.error(it)) }
}
