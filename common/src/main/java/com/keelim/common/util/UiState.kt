/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.common.util

data class UiState<T>(
    val loading: Boolean = false,
    val exception: Throwable? = null,
    val data: T? = null
) {
    val hasError: Boolean
        get() = exception != null

    val initialLoad: Boolean
        get() = data == null && loading && !hasError

    @Suppress("UNCHECKED_CAST")
    fun <T> getOrThrow(): T {
        throwOnFailure()
        return data as T
    }

    private fun throwOnFailure() {
        if (hasError) throw exception!!
    }

    companion object {
        fun <T> loading(): UiState<T> = UiState(loading = true)

        fun <T> success(value: T): UiState<T> = UiState(data = value)

        fun <T> failure(exception: Throwable): UiState<T> = UiState(exception = exception)
    }
}
