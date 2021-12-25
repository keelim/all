package com.keelim.mygrade.ui.center

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.db.entity.History
import com.keelim.data.repository.IoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterViewModel @Inject constructor(
    private val ioRepository: IoRepository
) : ViewModel() {
    fun saveHistory(query: String) = viewModelScope.launch {
        ioRepository.insertHistories(
            History(
                "",
                0,
                0f,
                0f,
                0,
                0f,
                "1"
            )
        )
    }
}