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
package com.keelim.data.model

import kotlinx.serialization.SerialName

data class RealtimeArrival(
    @SerialName("arvlCd")
    val arvlCd: String? = null,
    @SerialName("arvlMsg2")
    val arvlMsg2: String? = null,
    @SerialName("arvlMsg3")
    val arvlMsg3: String? = null,
    @SerialName("barvlDt")
    val barvlDt: String? = null,
    @SerialName("beginRow")
    val beginRow: Any? = null,
    @SerialName("bstatnId")
    val bstatnId: String? = null,
    @SerialName("bstatnNm")
    val bstatnNm: String? = null,
    @SerialName("btrainNo")
    val btrainNo: String? = null,
    @SerialName("btrainSttus")
    val btrainSttus: String? = null,
    @SerialName("curPage")
    val curPage: Any? = null,
    @SerialName("endRow")
    val endRow: Any? = null,
    @SerialName("ordkey")
    val ordkey: String? = null,
    @SerialName("pageRow")
    val pageRow: Any? = null,
    @SerialName("recptnDt")
    val recptnDt: String? = null,
    @SerialName("rowNum")
    val rowNum: Int? = null,
    @SerialName("selectedCount")
    val selectedCount: Int? = null,
    @SerialName("statnFid")
    val statnFid: String? = null,
    @SerialName("statnId")
    val statnId: String? = null,
    @SerialName("statnList")
    val statnList: String? = null,
    @SerialName("statnNm")
    val statnNm: String? = null,
    @SerialName("statnTid")
    val statnTid: String? = null,
    @SerialName("subwayHeading")
    val subwayHeading: String? = null,
    @SerialName("subwayId")
    val subwayId: Int = 0,
    @SerialName("subwayList")
    val subwayList: String? = null,
    @SerialName("subwayNm")
    val subwayNm: Any? = null,
    @SerialName("totalCount")
    val totalCount: Int? = null,
    @SerialName("trainCo")
    val trainCo: Any? = null,
    @SerialName("trainLineNm")
    val trainLineNm: String? = null,
    @SerialName("updnLine")
    val updnLine: String? = null,
)
