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

import com.keelim.data.di.IoDispatcher
import com.keelim.data.model.Developer
import com.keelim.data.repository.setting.DeveloperRepository
import com.keelim.nandadiagnosis.domain.usecase.NonParamCoroutineUseCase
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class GetDevelopersUseCase @Inject constructor(
    private val conferenceRepository: DeveloperRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : NonParamCoroutineUseCase<List<Developer>>(dispatcher) {
    override suspend fun execute(): List<Developer> {
        return conferenceRepository.getDeveloper()
            .sortedBy { it.name }
    }
}
