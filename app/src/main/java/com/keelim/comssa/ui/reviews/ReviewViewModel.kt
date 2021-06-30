package com.keelim.comssa.ui.reviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.comssa.data.model.Review
import com.keelim.comssa.usecase.GetAllReviewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val getAllReviewsUseCase: GetAllReviewsUseCase,
) : ViewModel() {
    private val _review = MutableLiveData<List<Review>>(listOf())
    val review: LiveData<List<Review>> get() = _review

    fun fetchReview(dataId: String) = viewModelScope.launch {
        _review.value = getAllReviewsUseCase.invoke(dataId)
    }
}