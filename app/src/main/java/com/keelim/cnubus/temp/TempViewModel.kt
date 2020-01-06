package com.keelim.cnubus.temp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TempViewModel (): ViewModel() {
       var clickConverter = MutableLiveData<Unit>()

        fun onClickHandler(){
            clickConverter.value = Unit
        }
}