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
package com.keelim.nandadiagnosis.data.api

import com.keelim.nandadiagnosis.data.dto.VideoDto
import retrofit2.Response
import retrofit2.http.GET

interface VideoService {
  @GET("/v3/cbb293eb-e8b2-4079-ba82-472d1c0419d1")
  fun listVideos(): Response<VideoDto>
}
