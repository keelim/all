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
package com.keelim.comssa.ui.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.comssa.data.model.ReviewedData
import com.keelim.domain.comssa.GetUserReviewedDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserReviewedDataUseCase: GetUserReviewedDataUseCase
) : ViewModel() {
    private val _reviewedData = MutableStateFlow<List<ReviewedData>>(listOf())
    val reviewedData: Flow<List<ReviewedData>> = _reviewedData.asStateFlow()

    init {
        fetchReviewedData()
    }
    fun fetchReviewedData() = viewModelScope.launch {
        _reviewedData.emit(
            getUserReviewedDataUseCase()
        )
    }
}
