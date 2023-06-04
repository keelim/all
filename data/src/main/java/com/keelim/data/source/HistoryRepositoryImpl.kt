package com.keelim.data.source

import com.keelim.data.db.dao.HistoryDao
import com.keelim.data.di.ApplicationScope
import com.keelim.data.di.DefaultDispatcher
import com.keelim.data.source.local.History
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val localDataSource: HistoryDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : HistoryRepository {
    override fun observeAll(): Flow<List<History>> = localDataSource.observeAll()
    override suspend fun create(history: History): String {
        localDataSource.upsert(history)
        return history.uid.toString()
    }

    // todo please more implement
    override suspend fun complete(historyId: String, grade: String) {
        localDataSource.updateCompleted(historyId, grade)
    }


    // todo please more implement
    override suspend fun refresh() {
        localDataSource.deleteAll()
    }
}
