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
package com.keelim.data.repository.setting

import com.keelim.data.model.Developer
import javax.inject.Inject

class DeveloperRepositoryImpl @Inject constructor(

)  : DeveloperRepository {
    override suspend fun getDeveloper(): List<Developer> {
        return runCatching {
            listOf(
                Developer(
                    "김재현",
                    "https://avatars.githubusercontent.com/u/26667456?v=4",
                    "김재현",
                    "https://github.com/keelim"
                )
            )
        }.getOrDefault(
            listOf(
                Developer(
                    "김재현",
                    "https://avatars.githubusercontent.com/u/26667456?v=4",
                    "김재현",
                    "https://github.com/keelim"
                )
            )
        )
    }
}
