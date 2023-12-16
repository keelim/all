package com.keelim.ecocal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.commonAndroid.model.asSealedUiState
import com.keelim.data.source.firebase.EcocalEntries
import com.keelim.data.source.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

@HiltViewModel
class EcocalViewModel @Inject constructor(
    val firebaseRepository: FirebaseRepository,
): ViewModel() {
    val ref = firebaseRepository
        .getRef("update")
        .mapLatest { result ->
            Timber.d("result $result")
            result.getOrNull() ?: EcocalEntries(emptyList())
        }
        .asSealedUiState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SealedUiState.loading())
}
