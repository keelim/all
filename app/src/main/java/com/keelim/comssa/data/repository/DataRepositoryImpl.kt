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

import com.keelim.comssa.data.api.DataApi
import com.keelim.comssa.data.model.Data
import com.keelim.comssa.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
  private val dataApi: DataApi,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : DataRepository {
  override suspend fun getAllDatas(): List<Data> = withContext(ioDispatcher) {
    return@withContext dataApi.getAllData()
  }

  override suspend fun getData(dataIds: List<String>): List<Data> = withContext(ioDispatcher) {
    return@withContext dataApi.getDatas(dataIds)
  }
}
