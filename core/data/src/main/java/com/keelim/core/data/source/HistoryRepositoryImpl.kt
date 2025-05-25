package com.keelim.core.data.source


import com.keelim.core.database.repository.HistoryRepository
import com.keelim.core.network.Dispatcher
import com.keelim.core.network.KeelimDispatchers
import com.keelim.core.network.di.ApplicationScope
import com.keelim.shared.data.database.dao.HistoryDao
import com.keelim.shared.data.database.dao.TimerHistoryDao
import com.keelim.shared.data.database.model.History
import com.keelim.shared.data.database.model.SimpleHistory
import com.keelim.shared.data.database.model.TimerHistory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val localDataSource: HistoryDao,
    private val timerHistoryDataSource: TimerHistoryDao,
    @Dispatcher(KeelimDispatchers.IO) private val io: CoroutineDispatcher,
    @Dispatcher(KeelimDispatchers.DEFAULT) private val default: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : HistoryRepository {
    override fun observeAll(): Flow<List<History>> =
        localDataSource.observeAll()

    override fun observeSimpleHistories(): Flow<List<SimpleHistory>> =
        localDataSource.observeSimpleHistories()

    override fun observeTimerHistories(): Flow<List<TimerHistory>> =
        timerHistoryDataSource.observeAll()

    override suspend fun create(history: History): String {
        localDataSource.upsert(history)
        return history.uid.toString()
    }

    override suspend fun create(subject: String, grade: String, point: String): Boolean {
        return try {
            withContext(io) {
                val (gradeRank, totalRank) = point.replace(" ", "").split("/")
                SimpleHistory(
                    subject = subject,
                    grade = grade,
                    gradeRank = gradeRank.toInt(),
                    totalRank = totalRank.toInt(),
                ).also { history ->
                    localDataSource.upsertSimpleHistory(history)
                }
            }
            true
        } catch (throwable: Throwable) {
            Timber.e(throwable.message.toString())
            false
        }
    }

    override suspend fun complete(historyId: String, grade: String) {
        localDataSource.updateCompleted(historyId, grade)
    }

    override suspend fun completedTimerHistory(historyId: String) {
        timerHistoryDataSource
            .updateCompleted(historyId)
    }

    override suspend fun refresh() {
        localDataSource.deleteAll()
    }
}
