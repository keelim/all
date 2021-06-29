package com.keelim.comssa.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class HomeViewModel : ViewModel() {
    private val _text = MutableLiveData("This is home Fragment")
    val text: LiveData<String> get() = _text
}