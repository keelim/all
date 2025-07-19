package com.keelim.nandadiagnosis.ui.screen.length

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.repository.LengthRepository
import com.keelim.model.LengthRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LengthViewModel @Inject constructor(
    private val repository: LengthRepository
) : ViewModel() {
    private val _records = MutableStateFlow<List<LengthRecord>>(emptyList())
    val records: StateFlow<List<LengthRecord>> = _records.asStateFlow()

    fun fetchRecords() {
        repository.getAllRecords()
            .onEach { _records.value = it }
            .launchIn(viewModelScope)
    }

    fun addRecord(record: LengthRecord) {
        viewModelScope.launch {
            repository.addRecord(record)
            fetchRecords()
        }
    }

    fun deleteRecord(date: String) {
        viewModelScope.launch {
            repository.deleteRecord(date)
            fetchRecords()
        }
    }
} 