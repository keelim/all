package com.keelim.mygrade.testutil

import com.keelim.data.repository.DefaultTaskRepository
import com.keelim.data.repository.HistoryRepository
import com.keelim.data.repository.NoteRepository
import com.keelim.data.repository.StudyAnalyticsRepository
import com.keelim.model.DailyStudyStats
import com.keelim.model.LocalTask
import com.keelim.model.Notices
import com.keelim.model.SimpleHistory
import com.keelim.model.SubjectStudyStats
import com.keelim.model.TimerHistoryModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeStudyAnalyticsRepository(
    dailyStats: List<DailyStudyStats> = emptyList(),
    subjectStats: List<SubjectStudyStats> = emptyList(),
    totalStudySeconds: Int = 0,
    studyDaysCount: Int = 0,
    currentStreak: Int = 0,
) : StudyAnalyticsRepository {
    val dailyStatsFlow = MutableStateFlow(dailyStats)
    val subjectStatsFlow = MutableStateFlow(subjectStats)
    val totalStudySecondsFlow = MutableStateFlow(totalStudySeconds)
    val studyDaysCountFlow = MutableStateFlow(studyDaysCount)
    val currentStreakFlow = MutableStateFlow(currentStreak)
    val recordedSessions = mutableListOf<Pair<String, Int>>()

    override fun getDailyStats(): Flow<List<DailyStudyStats>> = dailyStatsFlow

    override fun getSubjectStats(): Flow<List<SubjectStudyStats>> = subjectStatsFlow

    override fun getTotalStudySeconds(): Flow<Int> = totalStudySecondsFlow

    override fun getStudyDaysCount(): Flow<Int> = studyDaysCountFlow

    override fun getCurrentStreak(): Flow<Int> = currentStreakFlow

    override suspend fun recordSession(subject: String, durationSeconds: Int) {
        recordedSessions += subject to durationSeconds
    }
}

class FakeNoteRepository(
    initialNotes: Result<List<Notices>> = Result.success(emptyList()),
    private val noteDetailResult: Result<Notices> = Result.failure(IllegalStateException("Missing note")),
    var updateResult: Result<Unit> = Result.success(Unit),
    private var deleteResult: Result<Unit> = Result.success(Unit),
) : NoteRepository {
    val noteListFlow = MutableStateFlow(initialNotes)
    val updatedNotes = mutableListOf<Notices>()
    val deletedNotes = mutableListOf<Notices>()

    override fun getNoteList(): Flow<Result<List<Notices>>> = noteListFlow

    override suspend fun getNoteDetail(id: Int): Result<Notices> = noteDetailResult

    override suspend fun updateNote(notes: Notices): Result<Unit> {
        updatedNotes += notes
        return updateResult
    }

    override suspend fun deleteNoteList(notes: Notices): Result<Unit> {
        deletedNotes += notes
        return deleteResult
    }
}

class FakeHistoryRepository(
    simpleHistories: List<SimpleHistory> = emptyList(),
    timerHistories: List<TimerHistoryModel> = emptyList(),
    private val createResult: Boolean = false,
) : HistoryRepository {
    val simpleHistoriesFlow = MutableStateFlow(simpleHistories)
    val timerHistoriesFlow = MutableStateFlow(timerHistories)
    val createdHistories = mutableListOf<Triple<String, String, String>>()
    val deletedTimerHistoryIds = mutableListOf<Int>()
    val updatedDescriptions = mutableListOf<Pair<Int, String>>()
    val createdTimerHistories = mutableListOf<Triple<Int, Int, Int>>()
    var deleteAllTimerHistoriesCallCount = 0

    override fun observeSimpleHistories(): Flow<List<SimpleHistory>> = simpleHistoriesFlow

    override fun observeTimerHistories(): Flow<List<TimerHistoryModel>> = timerHistoriesFlow

    override suspend fun create(subject: String, grade: String, point: String): Boolean {
        createdHistories += Triple(subject, grade, point)
        return createResult
    }

    override suspend fun complete(historyId: String, grade: String) = Unit

    override suspend fun completedTimerHistory(historyId: Int) = Unit

    override suspend fun deleteTimerHistory(historyId: Int) {
        deletedTimerHistoryIds += historyId
    }

    override suspend fun deleteAllTimerHistories() {
        deleteAllTimerHistoriesCallCount += 1
    }

    override suspend fun updateTimerHistoryDescription(historyId: Int, description: String) {
        updatedDescriptions += historyId to description
    }

    override suspend fun createTimerHistory(hours: Int, minutes: Int, seconds: Int, description: String) {
        createdTimerHistories += Triple(hours, minutes, seconds)
    }

    override suspend fun refresh() = Unit
}

class FakeDefaultTaskRepository(
    initialTasks: List<LocalTask> = emptyList(),
) : DefaultTaskRepository {
    val tasksFlow = MutableStateFlow(initialTasks)
    val upsertedTasks = mutableListOf<LocalTask>()
    val deletedTasks = mutableListOf<LocalTask>()
    val completedTaskIds = mutableListOf<String>()
    val createdTasks = mutableListOf<Pair<String, String>>()
    var createCallCount = 0
    var refreshCallCount = 0
    var clearCallCount = 0

    override fun observeAll(): Flow<List<LocalTask>> = tasksFlow

    override suspend fun create() {
        createCallCount += 1
    }

    override suspend fun create(title: String, description: String): String {
        createdTasks += title to description
        return "created-id"
    }

    override suspend fun complete(taskId: String) {
        completedTaskIds += taskId
    }

    override suspend fun upsert(task: LocalTask) {
        upsertedTasks += task
    }

    override fun delete(task: LocalTask) {
        deletedTasks += task
    }

    override suspend fun refresh() {
        refreshCallCount += 1
    }

    override fun clear() {
        clearCallCount += 1
        tasksFlow.value = emptyList()
    }
}
