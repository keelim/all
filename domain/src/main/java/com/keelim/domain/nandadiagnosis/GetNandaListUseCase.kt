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
package com.keelim.domain.nandadiagnosis

import com.keelim.data.db.entity.NandaEntity2
import com.keelim.data.repository.NandaIORepository
import javax.inject.Inject

class GetNandaListUseCase @Inject constructor(
    private val nandaIoRepository: NandaIORepository,
) {

    suspend operator fun invoke(): List<NandaEntity2> {
        return nandaIoRepository.getLocalNandaList()
    }
}
