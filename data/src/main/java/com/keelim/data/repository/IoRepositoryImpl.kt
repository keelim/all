package com.keelim.data.repository

import com.keelim.data.db.AppDatabase
import com.keelim.data.db.entity.History
import com.keelim.data.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import javax.inject.Inject

class IoRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val db: AppDatabase
) : IoRepository {
    override suspend fun insertHistories(history: History) = withContext(ioDispatcher) {
        db.historyDao().insertHistories(history)
    }

    override suspend fun updateHistories(history: History) = withContext(ioDispatcher) {
        db.historyDao().updateHistories(history)
    }

    override suspend fun deleteHistories(history: History) = withContext(ioDispatcher) {
        db.historyDao().updateHistories(history)
    }

    override fun loadHistoriesById(uid: Int): Flow<History> = db
        .historyDao()
        .loadHistoriesById(uid)
        .distinctUntilChanged()

    override fun loadHistoriesBySubjects(subject: String): Flow<List<History>> = db
        .historyDao()
        .loadHistoriesBySubjects(subject)
        .distinctUntilChanged()


    override val all: Flow<List<History>> = db
        .historyDao()
        .getAll()
        .distinctUntilChanged()
}