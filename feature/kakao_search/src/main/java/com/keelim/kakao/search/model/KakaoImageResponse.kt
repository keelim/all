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
package com.keelim.kakao.search.model

import com.google.gson.annotations.SerializedName

data class KakaoImageResponse(
  @SerializedName("collection")
  val collection: String,
  @SerializedName("datetime")
  val datetime: String,
  @SerializedName("display_sitename")
  val displaySiteName: String,
  @SerializedName("doc_url")
  val docUrl: String,
  @SerializedName("height")
  val height: Int,
  @SerializedName("image_url")
  val imageUrl: String,
  @SerializedName("thumbnail_url")
  val thumbnailUrl: String,
  @SerializedName("width")
  val width: Int,
)
