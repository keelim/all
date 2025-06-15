package com.keelim.commonAndroid.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.core.network.Dispatcher
import com.keelim.core.network.KeelimDispatchers
import com.keelim.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    @Dispatcher(KeelimDispatchers.DEFAULT) private val default: CoroutineDispatcher,
    private val firebaseRepository: FirebaseRepository,
) : ViewModel() {
    val newNotification = suspend { firebaseRepository.getRef("new_notification") }
        .asFlow()
        .flowOn(default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), "")
}
