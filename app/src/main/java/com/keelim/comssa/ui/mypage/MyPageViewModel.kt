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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.comssa.data.model.ReviewedData
import com.keelim.comssa.usecase.GetUserReviewedDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
  private val getUserReviewedDataUseCase: GetUserReviewedDataUseCase
) : ViewModel() {
  private val _reviewedData = MutableLiveData<List<ReviewedData>>(listOf())
  val reviewedData: LiveData<List<ReviewedData>> get() = _reviewedData

  fun fetchReviewedData() = viewModelScope.launch {
    _reviewedData.value = getUserReviewedDataUseCase.invoke()
  }
}
