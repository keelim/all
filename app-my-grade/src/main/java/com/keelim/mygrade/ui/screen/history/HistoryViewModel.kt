package com.keelim.mygrade.ui.screen.history

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.keelim.common.di.DefaultDispatcher
import com.keelim.core.data.source.HistoryRepository
import com.keelim.core.database.model.SimpleHistory
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

@Stable
@HiltViewModel
class HistoryViewModel @Inject constructor(
    historyRepository: HistoryRepository,
    @DefaultDispatcher val disPatcher: CoroutineDispatcher,
) : ViewModel() {
    val histories: Flow<PersistentList<GradeHistory>> =
        historyRepository
            .observeSimpleHistories()
            .mapLatest { it -> it.map { it.toGradeHistory() }.toPersistentList() }
            .catch { emit(persistentListOf()) }
            .distinctUntilChanged()
            .flowOn(disPatcher)
}
