/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
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
package com.keelim.cnubus.utils

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

interface ErrorViewModel {
    val error: SharedFlow<Throwable>
    fun emitError(throwable: Throwable)
}

class ErrorViewModelImpl @Inject constructor() : ErrorViewModel {
    private val _error = MutableSharedFlow<Throwable>()
    override val error: SharedFlow<Throwable> = _error
    override fun emitError(throwable: Throwable) {
        Firebase.crashlytics.recordException(throwable)
        _error.tryEmit(throwable)
    }
}
