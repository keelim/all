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
package com.keelim.nandadiagnosis.compose.ui

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out R> {

  data class Success<out T>(val data: T) : Result<T>()
  data class Error(val exception: Throwable) : Result<Nothing>()
  object Loading : Result<Nothing>()

  override fun toString(): String {
    return when (this) {
      is Success<*> -> "Success[data=$data]"
      is Error -> "Error[exception=$exception]"
      Loading -> "Loading"
    }
  }
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val Result<*>.succeeded
  get() = this is Result.Success && data != null

fun <T> Result<T>.successOr(fallback: T): T {
  return (this as? Result.Success<T>)?.data ?: fallback
}

val <T> Result<T>.data: T?
  get() = (this as? Result.Success)?.data

inline fun <R, T> Result<T>.map(transform: (T) -> R): Result<R> {
  return when (this) {
    is Result.Success -> Result.Success(transform(data))
    is Result.Error -> Result.Error(exception)
    Result.Loading -> Result.Loading
  }
}

inline fun <R, T> Result<T>.mapCatching(transform: (T) -> R): Result<R> {
  return when (this) {
    is Result.Success -> {
      try {
        Result.Success(transform(data))
      } catch (e: Throwable) {
        Result.Error(e)
      }
    }
    is Result.Error -> Result.Error(exception)
    Result.Loading -> Result.Loading
  }
}
