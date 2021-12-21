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
package com.keelim.cnubus.feature.map.ui

import com.keelim.cnubus.data.model.gps.Location

sealed class MapEvent {
    object UnInitialized : MapEvent()
    object Loading : MapEvent()
    data class MigrateSuccess(val data: List<Location>): MapEvent()
    data class Error(val message: String = "에러가 발생하였습니다.") : MapEvent()
}
