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
package com.keelim.cnubus.data.model.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddressInfo(
    @SerializedName("fullAddress")
    @Expose
    val fullAddress: String?,
    @SerializedName("addressType")
    @Expose
    val addressType: String?,
    @SerializedName("city_do")
    @Expose
    val cityDo: String?,
    @SerializedName("gu_gun")
    @Expose
    val guGun: String?,
    @SerializedName("eup_myun")
    @Expose
    val eupMyun: String?,
    @SerializedName("adminDong")
    @Expose
    val adminDong: String?,
    @SerializedName("adminDongCode")
    @Expose
    val adminDongCode: String?,
    @SerializedName("legalDong")
    @Expose
    val legalDong: String?,
    @SerializedName("legalDongCode")
    @Expose
    val legalDongCode: String?,
    @SerializedName("ri")
    @Expose
    val ri: String?,
    @SerializedName("bunji")
    @Expose
    val bunji: String?,
    @SerializedName("roadName")
    @Expose
    val roadName: String?,
    @SerializedName("buildingIndex")
    @Expose
    val buildingIndex: String?,
    @SerializedName("buildingName")
    @Expose
    val buildingName: String?,
    @SerializedName("mappingDistance")
    @Expose
    val mappingDistance: String?,
    @SerializedName("roadCode")
    @Expose
    val roadCode: String?
)
