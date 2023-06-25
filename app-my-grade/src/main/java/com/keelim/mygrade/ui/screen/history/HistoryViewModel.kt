package com.keelim.mygrade.ui.screen.history

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

data class GradeHistory(
    val date: String,
    val grade: String,
    val myGrade: Int,
    val totalStudent: Int,
)

@HiltViewModel
class HistoryViewModel @Inject constructor() : ViewModel() {
    val histories: Flow<PersistentList<GradeHistory>> = flow {
        emit(persistentListOf())
    }
}
