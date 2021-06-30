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