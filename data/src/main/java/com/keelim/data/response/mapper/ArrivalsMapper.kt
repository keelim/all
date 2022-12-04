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
package com.keelim.data.response.mapper

import com.keelim.data.model.ArrivalInformation
import com.keelim.data.model.Subway
import com.keelim.data.response.RealtimeArrival
import java.text.SimpleDateFormat
import java.util.Locale

private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.'0'", Locale.KOREA)
private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.KOREA)

private const val INVALID_FIELD = "-"

fun RealtimeArrival.toArrivalInformation(): ArrivalInformation =
    ArrivalInformation(
        subway = Subway.findById(subwayId),
        direction = trainLineNm?.split("-")
            ?.get(1)
            ?.trim()
            ?: INVALID_FIELD,
        destination = bstatnNm ?: INVALID_FIELD,
        message = arvlMsg2
            ?.replace(statnNm.toString(), "당역")
            ?.replace("[\\[\\]]".toRegex(), "")
            ?: INVALID_FIELD,
        updatedAt = recptnDt
            ?.let { apiDateFormat.parse(it) }
            ?.let { dateFormat.format(it) }
            ?: INVALID_FIELD
    )

fun List<RealtimeArrival>.toArrivalInformation(): List<ArrivalInformation> =
    map { it.toArrivalInformation() }
