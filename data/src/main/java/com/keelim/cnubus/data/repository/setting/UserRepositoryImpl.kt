/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.cnubus.data.repository.setting

import androidx.room.withTransaction
import com.keelim.cnubus.data.db.AppDatabase
import com.keelim.cnubus.data.db.DataStoreManager
import com.keelim.cnubus.data.db.entity.History
import com.keelim.cnubus.data.model.User
import com.keelim.cnubus.di.IoDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    override suspend fun deleteHistoryAll() = db.withTransaction {
        db.daoHistory().deleteAll()
    }
}
