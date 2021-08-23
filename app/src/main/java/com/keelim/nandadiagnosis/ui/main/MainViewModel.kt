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
package com.keelim.nandadiagnosis.ui.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.keelim.nandadiagnosis.data.network.NandaService
import com.keelim.nandadiagnosis.domain.GetAppThemeUseCase
import com.keelim.nandadiagnosis.domain.SetAppThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
  getTheme: GetAppThemeUseCase,
  private val setTheme: SetAppThemeUseCase,
) : ViewModel() {
  val theme: LiveData<Int> = getTheme.appTheme.asLiveData()

  fun setAppTheme(theme: Int) = viewModelScope.launch {
    setTheme.invoke(theme)
  }

  val loading = mutableStateOf(false)

  fun loading() = viewModelScope.launch {
    loading.value = true
    delay(1000)
    loading.value = false
  }
}
