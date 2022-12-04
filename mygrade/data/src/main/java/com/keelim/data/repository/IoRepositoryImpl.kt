package com.keelim.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.keelim.common.Dispatcher
import com.keelim.common.KeelimDispatchers.IO
import com.keelim.data.api.ApiRequestFactory
import com.keelim.data.db.dao.HistoryDao
import com.keelim.data.db.dao.SimpleHistoryDao
import com.keelim.data.db.entity.History
import com.keelim.data.db.entity.SimpleHistory
import com.keelim.data.db.paging.DBPagingSource
import com.keelim.data.model.notification.Notification
import com.keelim.data.model.notification.mapepr.toNotification
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class IoRepositoryImpl @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val apiRequestFactory: ApiRequestFactory,
    private val historyDao: HistoryDao,
    private val simpleHistoryDao: SimpleHistoryDao,
) : IoRepository {
    override suspend fun insertHistories(history: History) = withContext(ioDispatcher) {
        historyDao.insertHistories(history)
    }

    override suspend fun updateHistories(history: History) = withContext(ioDispatcher) {
        historyDao.updateHistories(history)
    }

    override suspend fun deleteHistories(history: History) = withContext(ioDispatcher) {
        historyDao.updateHistories(history)
    }

    override suspend fun getAllHistories(): List<History> = withContext(ioDispatcher) {
        return@withContext try {
            historyDao.getAllHistory()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun loadHistoriesById(uid: Int): Flow<History> =
        historyDao.loadHistoriesById(uid).distinctUntilChanged()

    override fun loadHistoriesBySubjects(subject: String): Flow<List<History>> =
        historyDao.loadHistoriesBySubjects(subject).distinctUntilChanged()


    override val all: Flow<List<History>> = historyDao.getAll().distinctUntilChanged()

    override val simpleAll: Flow<List<SimpleHistory>> = simpleHistoryDao.getAll()

    override fun getAllPaging(): Flow<PagingData<History>> {
        return Pager(config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { DBPagingSource(historyDao) }).flow.distinctUntilChanged()
    }

    override suspend fun insertSimpleHistories(history: SimpleHistory) = withContext(ioDispatcher) {
        simpleHistoryDao.insertHistories(history)
    }

    override suspend fun getAllSimpleHistories(history: SimpleHistory) = withContext(ioDispatcher) {
        simpleHistoryDao.getAllSimpleHistory()
    }

    override fun getNotification(): Flow<List<Notification>> = flow {
        apiRequestFactory.retrofit.getNotification()
            .takeIf { it.isSuccessful && it.body() != null }
            ?.let {
                emit(
                    it.body()?.toNotification() ?: emptyList()
                )
            } ?: run {
            emit(emptyList())
        }
    }
}
