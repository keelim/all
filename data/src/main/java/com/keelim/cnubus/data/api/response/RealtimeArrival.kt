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
package com.keelim.cnubus.data.api.response

import com.google.gson.annotations.SerializedName

data class RealtimeArrival(
    @SerializedName("arvlCd")
    val arvlCd: String? = null,
    @SerializedName("arvlMsg2")
    val arvlMsg2: String? = null,
    @SerializedName("arvlMsg3")
    val arvlMsg3: String? = null,
    @SerializedName("barvlDt")
    val barvlDt: String? = null,
    @SerializedName("beginRow")
    val beginRow: Any? = null,
    @SerializedName("bstatnId")
    val bstatnId: String? = null,
    @SerializedName("bstatnNm")
    val bstatnNm: String? = null,
    @SerializedName("btrainNo")
    val btrainNo: String? = null,
    @SerializedName("btrainSttus")
    val btrainSttus: String? = null,
    @SerializedName("curPage")
    val curPage: Any? = null,
    @SerializedName("endRow")
    val endRow: Any? = null,
    @SerializedName("ordkey")
    val ordkey: String? = null,
    @SerializedName("pageRow")
    val pageRow: Any? = null,
    @SerializedName("recptnDt")
    val recptnDt: String? = null,
    @SerializedName("rowNum")
    val rowNum: Int? = null,
    @SerializedName("selectedCount")
    val selectedCount: Int? = null,
    @SerializedName("statnFid")
    val statnFid: String? = null,
    @SerializedName("statnId")
    val statnId: String? = null,
    @SerializedName("statnList")
    val statnList: String? = null,
    @SerializedName("statnNm")
    val statnNm: String? = null,
    @SerializedName("statnTid")
    val statnTid: String? = null,
    @SerializedName("subwayHeading")
    val subwayHeading: String? = null,
    @SerializedName("subwayId")
    val subwayId: Int = 0,
    @SerializedName("subwayList")
    val subwayList: String? = null,
    @SerializedName("subwayNm")
    val subwayNm: Any? = null,
    @SerializedName("totalCount")
    val totalCount: Int? = null,
    @SerializedName("trainCo")
    val trainCo: Any? = null,
    @SerializedName("trainLineNm")
    val trainLineNm: String? = null,
    @SerializedName("updnLine")
    val updnLine: String? = null
)
