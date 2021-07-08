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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.comssa.data.model.Data
import com.keelim.comssa.data.model.FeaturedData
import com.keelim.comssa.domain.GetAllDatasUseCase
import com.keelim.comssa.domain.GetRandomFeatureDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val getRandomFeatureDataUseCase: com.keelim.comssa.domain.GetRandomFeatureDataUseCase,
  private val getAllDatasUseCase: com.keelim.comssa.domain.GetAllDatasUseCase,
) : ViewModel() {
  private val _randomData = MutableLiveData<FeaturedData>()
  val randomData: LiveData<FeaturedData> get() = _randomData

  private val _allData = MutableLiveData<List<Data>>(listOf())
  val allData: LiveData<List<Data>> get() = _allData

  fun fetchData() = viewModelScope.launch {
    _randomData.value = getRandomFeatureDataUseCase.invoke()
    _allData.value = getAllDatasUseCase.invoke()
  }
}
