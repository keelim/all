package com.keelim.nandadiagnosis.ui.auth.profile

import androidx.lifecycle.viewModelScope
import com.keelim.nandadiagnosis.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal  class ProfileViewModel: BaseViewModel() {
    override fun fetchData(): Job = viewModelScope.launch{

    }
}