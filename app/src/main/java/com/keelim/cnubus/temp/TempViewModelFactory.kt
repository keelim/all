package com.keelim.cnubus.temp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TempViewModelFactory : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TempViewModel() as T
    }

}