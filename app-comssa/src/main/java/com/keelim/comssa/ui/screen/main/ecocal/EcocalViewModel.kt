package com.keelim.comssa.ui.screen.main.ecocal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.commonAndroid.model.asSealedUiState
import com.keelim.data.source.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

@HiltViewModel
class EcocalViewModel @Inject constructor(
    firebaseRepository: FirebaseRepository,
) : ViewModel() {
    val ref = firebaseRepository
        .getRef("2023-12")
        .mapLatest { result ->
            Timber.d("result $result")
            result.getOrThrow()
        }.catch { throwable ->
            Timber.e(throwable)
            emitAll(emptyFlow())
        }
        .asSealedUiState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SealedUiState.loading())
}
