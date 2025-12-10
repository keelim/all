package com.keelim.core.data.source

import com.keelim.common.Dispatcher
import com.keelim.common.KeelimDispatchers
import com.keelim.common.di.ApplicationScope
import com.keelim.core.database.mapper.toSimpleHistoryModel
import com.keelim.core.database.mapper.toTimerHistoryModels
import com.keelim.data.repository.HistoryRepository
import com.keelim.model.TimerHistoryModel
import com.keelim.shared.data.database.dao.HistoryDao
import com.keelim.shared.data.database.dao.TimerHistoryDao
import com.keelim.shared.data.database.model.SimpleHistory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun observeSimpleHistories(): Flow<List<com.keelim.model.SimpleHistory>> =
        localDataSource.observeSimpleHistories()
            .map {
                it.toSimpleHistoryModel()
            }

    override fun observeTimerHistories(): Flow<List<TimerHistoryModel>> =
        timerHistoryDataSource.observeAll()
            .map { it.toTimerHistoryModels() }


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

    override suspend fun completedTimerHistory(historyId: Int) {
        timerHistoryDataSource.updateCompleted(historyId)
    }

    override suspend fun deleteTimerHistory(historyId: Int) {
        withContext(io) {
            timerHistoryDataSource.deleteById(historyId)
        }
    }

    override suspend fun deleteAllTimerHistories() {
        withContext(io) {
            timerHistoryDataSource.deleteAll()
        }
    }

    override suspend fun updateTimerHistoryDescription(historyId: Int, description: String) {
        withContext(io) {
            timerHistoryDataSource.updateDescription(historyId, description)
        }
    }

    override suspend fun createTimerHistory(
        hours: Int,
        minutes: Int,
        seconds: Int,
        description: String,
    ) {
        withContext(io) {
            timerHistoryDataSource.upsert(
                com.keelim.shared.data.database.model.TimerHistory(
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds,
                    description = description,
                    isCompleted = true,
                )
            )
        }
    }

    override suspend fun refresh() {
        localDataSource.deleteAll()
    }
}
