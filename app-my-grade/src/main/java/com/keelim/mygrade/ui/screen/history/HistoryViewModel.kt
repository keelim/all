package com.keelim.mygrade.ui.screen.history

import androidx.lifecycle.ViewModel
import com.keelim.data.di.DefaultDispatcher
import com.keelim.data.source.HistoryRepository
import com.keelim.data.source.local.SimpleHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

data class GradeHistory(
    val subject: String,
    val date: String,
    val grade: String,
    val myGrade: Int,
    val totalStudent: Int,
)

fun SimpleHistory.toGradeHistory(): GradeHistory {
    // TODO: formatter 사용하기
    return GradeHistory(
        subject = subject,
        date = date.split("T")[0],
        grade = grade,
        myGrade = gradeRank,
        totalStudent = totalRank,
    )
}

@HiltViewModel
class HistoryViewModel @Inject constructor(
    historyRepository: HistoryRepository,
    @DefaultDispatcher val disPatcher: CoroutineDispatcher,
) : ViewModel() {
    val histories: Flow<PersistentList<GradeHistory>> =
        historyRepository
            .observeSimpleHistories()
            .mapLatest { it.map { it.toGradeHistory() }.toPersistentList() }
            .catch { emit(persistentListOf()) }
            .distinctUntilChanged()
            .flowOn(disPatcher)
}
