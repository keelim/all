package com.keelim.nandadiagnosis.ui.main.search

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.keelim.nandadiagnosis.model.SingleLiveEvent

class TempViewModel : ViewModel(){
    private val _query = MutableLiveData<List<String>>()

}

