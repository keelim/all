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
package com.keelim.cnubus.ui.main

import androidx.lifecycle.*
import com.keelim.cnubus.domain.GetAppThemeUseCase
import com.keelim.cnubus.domain.SetAppThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTheme: GetAppThemeUseCase,
    private val setTheme: SetAppThemeUseCase,
) : ViewModel() {
    val theme: LiveData<Int> = getTheme.appTheme.asLiveData()

    fun setAppTheme(theme: Int) = viewModelScope.launch {
        setTheme.invoke(theme)
    }

    private val _loading = MutableLiveData(false)
    val loading :LiveData<Boolean> = _loading

    fun loadingOn() = viewModelScope.launch {
        _loading.value = true
        delay(1500)
        _loading.value = false
    }
}
