package com.keelim.cnubus.data.repository.setting

import androidx.room.withTransaction
import com.keelim.cnubus.data.db.AppDatabase
import com.keelim.cnubus.data.db.DataStoreManager
import com.keelim.cnubus.data.db.entity.History
import com.keelim.cnubus.data.model.User
import com.keelim.cnubus.di.IoDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class UserRepositoryImpl @Inject constructor(
    @IoDispatcher val io: CoroutineDispatcher,
    private val dataStoreManager: DataStoreManager,
    private val db: AppDatabase,
) : UserRepository {
    override suspend fun getUserInformation(): Flow<User> = withContext(io) {
        flow {
            val id = dataStoreManager.getUserId("id")
            id.collect { userId ->
                emit(
                    User(
                        id = userId
                    )
                )
            }
        }
    }

    override suspend fun setUserInformation(name: String) {
        dataStoreManager.setUserId("id", name)
    }

    override fun getUserHistory(): Flow<List<History>> {
        return db.daoHistory().getHistoryAll()
    }

    override suspend fun getUserRawHistory(): List<History> = db.withTransaction {
        db.daoHistory().getHistoryRawAll()
    }
    override suspend fun insertHistory(history: History) = db.withTransaction {
        db.daoHistory().insertHistory(history)
    }

    override suspend fun deleteHistory(history: History) = db.withTransaction {
        db.daoHistory().deleteHistory(history)
    }

    override suspend fun deleteHistoryAll() = db.withTransaction{
        db.daoHistory().deleteAll()
    }
}