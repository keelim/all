package com.keelim.data.db.data.source.local

import com.keelim.core.database.model.History
import com.keelim.core.database.model.SimpleHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class FakeHistoryDao(initialTasks: List<History>) :
    com.keelim.core.database.dao.HistoryDao {

    private val _histories = initialTasks.toMutableList()
    private val historyStream = MutableStateFlow(_histories.toList())

    override fun observeAll(): Flow<List<History>> = historyStream
    override fun observeSimpleHistories(): Flow<List<SimpleHistory>> =
        flow {
            TODO("Not yet implemented")
        }

    override suspend fun upsert(history: History) {
        _histories.removeIf { it.uid == history.uid }
        _histories.add(history)
        historyStream.emit(_histories)
    }

    override suspend fun upsertSimpleHistory(history: SimpleHistory) {
        TODO("Not yet implemented")
    }

    override suspend fun upsertAll(histories: List<History>) {
        val newHistory = histories.map { it.uid }
        _histories.removeIf { newHistory.contains(it.uid) }
        _histories.addAll(histories)
    }

    override suspend fun updateCompleted(historyId: String, grade: String) {
        _histories.firstOrNull { it.grade == historyId }?.let { it.grade == grade }
        historyStream.emit(_histories)
    }

    override suspend fun deleteAll() {
        _histories.clear()
        historyStream.emit(_histories)
    }

    override suspend fun getPagingAll(
        page: Int,
        loadSize: Int
    ): List<History> {
        return emptyList()
    }
}
