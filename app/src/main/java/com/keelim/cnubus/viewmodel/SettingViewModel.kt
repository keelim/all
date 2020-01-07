package com.keelim.cnubus.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingViewModel (): ViewModel() {
    var clickConverter = MutableLiveData<Unit>()

    fun onClickHandler(){ //
        clickConverter.value = Unit
    }
}