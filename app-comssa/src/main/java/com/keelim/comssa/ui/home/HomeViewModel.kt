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
package com.keelim.comssa.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.model.Data
import com.keelim.data.model.FeaturedData
import com.keelim.domain.comssa.GetAllDatasUseCase
import com.keelim.domain.comssa.GetRandomFeatureDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val getRandomFeatureDataUseCase: GetRandomFeatureDataUseCase,
    private val getAllDatasUseCase: GetAllDatasUseCase,
) : ViewModel() {
    val randomData: StateFlow<FeaturedData?> =
        suspend { getRandomFeatureDataUseCase.invoke() }
            .asFlow()
            .catch {}
            .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val allData: StateFlow<List<Data>> =
        suspend { getAllDatasUseCase.invoke() }
            .asFlow()
            .catch {}
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
