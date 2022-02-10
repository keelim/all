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
package com.keelim.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    // 현재 ViewModel에서 진행하고 있는 작업이 있는지에 대한 여부
    private val _shouldUIinProgress = MutableStateFlow(false)
    val shouldUIinProgress: StateFlow<Boolean>
        get() = _shouldUIinProgress

    private val registeredWorks = mutableMapOf<Long, Boolean>()

    fun createUIProgressWork(): Long {
        val key = System.currentTimeMillis()
        registeredWorks[key] = true
        syncProgress()
        return key
    }

    fun reportProgressIsDone(key: Long) {
        // 해당 작업이 완료되었음을 보고함 -> LiveData 업데이트
        registeredWorks[key] = false
        syncProgress()
    }

    private fun syncProgress() = viewModelScope.launch {
        registeredWorks.values.removeAll { !it }
        _shouldUIinProgress.emit(registeredWorks.isNotEmpty())
    }
}
