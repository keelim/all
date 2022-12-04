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
package com.keelim.domain.setting

import com.keelim.cnubus.data.db.entity.History
import com.keelim.cnubus.data.model.User
import com.keelim.cnubus.data.repository.setting.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class UserUseCase(
    private val userRepository: UserRepository,
) {
    suspend fun getUserName(): Flow<User> = userRepository
        .getUserInformation()
        .distinctUntilChanged()

    suspend fun setUserName(name: String) {
        userRepository.setUserInformation(name)
    }

    fun getAllHistories(): Flow<List<History>> = userRepository.getUserHistory()
    suspend fun getAllRawHistories() = userRepository.getUserRawHistory()
    suspend fun insertHistory(history: History) = userRepository.insertHistory(history)
    suspend fun deleteHistory(history: History) = userRepository.deleteHistory(history)
    suspend fun deleteHistoryAll() = userRepository.deleteHistoryAll()
}
