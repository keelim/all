package com.keelim.comssa.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class MyPageViewModel:ViewModel() {
    private val _text = MutableLiveData<String>("This is dashboard Fragment")
    val text:LiveData<String> get() = _text

}