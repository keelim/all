package com.keelim.comssa.ui.reviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.comssa.data.model.Data
import com.keelim.comssa.data.model.DataReviews
import com.keelim.comssa.data.model.Review
import com.keelim.comssa.usecase.DeleteReviewUseCase
import com.keelim.comssa.usecase.GetAllDataReviewsUseCase
import com.keelim.comssa.usecase.SubmitReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val submitReviewUseCase: SubmitReviewUseCase,
    private val deleteReviewUseCase: DeleteReviewUseCase,
    private val getAllDataReviewsUseCase: GetAllDataReviewsUseCase
):ViewModel() {
    private val _review = MutableLiveData<Review>(Review())
    val review:LiveData<Review> get() = _review

    private val _reviewList = MutableLiveData<DataReviews>(DataReviews(null, listOf()))
    val reviewList:LiveData<DataReviews> get() = _reviewList

    fun submitReview(data: Data, content:String, score:Float) = viewModelScope.launch{
        _review.value = submitReviewUseCase.invoke(data, content, score)
    }

    fun deleteReview(review:Review) = viewModelScope.launch {
        deleteReviewUseCase.invoke(review)
    }

    fun getAllReview(dataId:String) = viewModelScope.launch {
        _reviewList.value = getAllDataReviewsUseCase.invoke(dataId)
    }
}