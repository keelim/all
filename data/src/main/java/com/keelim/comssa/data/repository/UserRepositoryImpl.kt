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
package com.keelim.comssa.data.repository

import com.keelim.comssa.data.api.UserApi
import com.keelim.comssa.data.model.User
import com.keelim.comssa.data.preference.PreferenceManager
import com.keelim.comssa.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
  private val userApi: UserApi,
  private val preferenceManager: PreferenceManager,
  @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : UserRepository {
  override suspend fun getUser(): User? {
    return preferenceManager.getString(KEY_USER_ID)?.let {
      User(it)
    }
  }

  override suspend fun saveUser(user: User) {
    val newvie = userApi.saveUser(user)
    preferenceManager.putString(KEY_USER_ID, newvie.id!!)
  }

  companion object {
    private const val KEY_USER_ID = "KEY_USER_ID"
  }
}
