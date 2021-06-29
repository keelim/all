package com.keelim.comssa.ui.reviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(

):ViewModel() {
    private val _text = MutableLiveData<String>("This is notifications Fragment")
    val text: LiveData<String> get() = _text
}