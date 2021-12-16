/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
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
package com.keelim.nandadiagnosis.domain

import androidx.paging.PagingData
import com.keelim.nandadiagnosis.data.db.entity.NandaEntity
import com.keelim.nandadiagnosis.data.repository.io.IORepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchListUseCase @Inject constructor(
  private val ioRepository: IORepository,
) {

  suspend operator fun invoke(query: String?): List<NandaEntity> {
    return ioRepository.getSearchList(query)
  }

  fun getFlowData(query: String): Flow<List<NandaEntity>> {
    return ioRepository.getSearchFlow(query)
  }

  fun getSearchFlow(query: String): Flow<PagingData<NandaEntity>> {
    return ioRepository.getTodoContentItemsByPaging(query)
  }
}
