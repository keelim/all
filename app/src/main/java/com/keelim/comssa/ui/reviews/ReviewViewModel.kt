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
package com.keelim.comssa.ui.reviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.comssa.data.model.Data
import com.keelim.comssa.data.model.DataReviews
import com.keelim.comssa.data.model.Review
import com.keelim.comssa.domain.DeleteReviewUseCase
import com.keelim.comssa.domain.GetAllDataReviewsUseCase
import com.keelim.comssa.domain.SubmitReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
  private val submitReviewUseCase: com.keelim.comssa.domain.SubmitReviewUseCase,
  private val deleteReviewUseCase: com.keelim.comssa.domain.DeleteReviewUseCase,
  private val getAllDataReviewsUseCase: com.keelim.comssa.domain.GetAllDataReviewsUseCase
) : ViewModel() {
  private val _review = MutableLiveData<Review>(Review())
  val review: LiveData<Review> get() = _review

  private val _reviewList = MutableLiveData<DataReviews>(DataReviews(null, listOf()))
  val reviewList: LiveData<DataReviews> get() = _reviewList

  fun submitReview(data: Data, content: String, score: Float) = viewModelScope.launch {
    _review.value = submitReviewUseCase.invoke(data, content, score)
  }

  fun deleteReview(review: Review) = viewModelScope.launch {
    deleteReviewUseCase.invoke(review)
  }

  fun getAllReview(dataId: String) = viewModelScope.launch {
    _reviewList.value = getAllDataReviewsUseCase.invoke(dataId)
  }
}
