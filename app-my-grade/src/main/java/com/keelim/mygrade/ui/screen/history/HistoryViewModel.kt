package com.keelim.mygrade.ui.screen.history

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.keelim.common.Dispatcher
import com.keelim.common.KeelimDispatchers
import com.keelim.commonAndroid.extensions.toUiDate
import com.keelim.data.repository.HistoryRepository
import com.keelim.model.SimpleHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.LocalDateTime
import jakarta.inject.Inject

data class GradeHistory(
    val subject: String,
    val date: String,
    val grade: String,
    val myGrade: Int,
    val totalStudent: Int,
)

fun SimpleHistory.toGradeHistory(): GradeHistory {
    return GradeHistory(
        subject = subject,
        date = date.toHistoryUiDate(),
        grade = grade,
        myGrade = gradeRank,
        totalStudent = totalRank,
    )
}

private fun String.toHistoryUiDate(): String {
    return runCatching { LocalDateTime.parse(this).toUiDate() }.getOrDefault(this)
}

@Stable
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HistoryViewModel @Inject constructor(
    historyRepository: HistoryRepository,
    @Dispatcher(KeelimDispatchers.DEFAULT) val disPatcher: CoroutineDispatcher,
) : ViewModel() {
    val histories: Flow<PersistentList<GradeHistory>> =
        historyRepository
            .observeSimpleHistories()
            .mapLatest { it -> it.map { it.toGradeHistory() }.toPersistentList() }
            .catch { emit(persistentListOf()) }
            .distinctUntilChanged()
            .flowOn(disPatcher)
}
