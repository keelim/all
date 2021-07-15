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
package com.keelim.reference_search

import com.google.gson.annotations.SerializedName

data class Reference(
  @SerializedName("itemId") val lastBuildDate: String,
  @SerializedName("total") val total: Int,
  @SerializedName("start") val start: Int,
  @SerializedName("display") val display: Int,
  @SerializedName("title") val title: String,
  @SerializedName("link") val link: String,
  @SerializedName("image") val image: String,
  @SerializedName("author") val author: String,
  @SerializedName("price") val price: Int,
  @SerializedName("discount") val discount: Int,
  @SerializedName("publisher") val publisher: String,
  @SerializedName("isbn") val isbn: Int,
  @SerializedName("description") val description: String,
  @SerializedName("pubdate") val pubdate: String,
)
