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
package com.keelim.cnubus.data.repository.station

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.keelim.cnubus.data.db.entity.StationEntity
import com.keelim.cnubus.data.db.entity.SubwayEntity
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StationApiImpl @Inject constructor() : StationApi {

    private val sheetReference = Firebase.storage.reference.child(STATION_DATA_FILE_NAME)

    override suspend fun getStationDataUpdatedTimeMillis(): Long =
        sheetReference.metadata.await().updatedTimeMillis

    override suspend fun getStationSubways(): List<Pair<StationEntity, SubwayEntity>> {
        val downloadSizeBytes = sheetReference.metadata.await().sizeBytes
        val byteArray = sheetReference.getBytes(downloadSizeBytes).await()

        return byteArray.decodeToString()
            .lines()
            .drop(1)
            .map { it.split(",") }
            .map { StationEntity(it[1]) to SubwayEntity(it[0].toInt()) }
    }

    companion object {
        private const val STATION_DATA_FILE_NAME = "station_data.csv"
    }
}
