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
package com.keelim.nandadiagnosis.domain.favorite

import com.keelim.nandadiagnosis.data.repository.IORepository
import javax.inject.Inject

class FavoriteUpdateUseCase @Inject constructor(
    private val ioRepository: IORepository,
) {

    suspend operator fun invoke(favorite: Int, id: Int) {
        when (favorite) {
            1 -> ioRepository.updateFavorite(0, id)
            0 -> ioRepository.updateFavorite(1, id)
            else -> Unit
        }
    }
}
