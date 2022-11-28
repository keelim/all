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
package com.keelim.nandadiagnosis.data.network

import com.keelim.nandadiagnosis.data.db.entity.NandaEntity2
import com.keelim.nandadiagnosis.data.response.NandaResponse
import com.keelim.nandadiagnosis.data.response.NandasResponse
import com.squareup.okhttp.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url

interface NandaService {
    @GET("api/v1/nanda_information/{name}")
    suspend fun getNanfaInformation(@Path("name") type: String): List<NandaEntity2>

    @Streaming
    @GET
    suspend fun getDatabase(@Url fileUrl: String): Response<ResponseBody>

    @GET("nandas")
    suspend fun getNandas(): Response<NandasResponse>

    @GET("nandas/{nandas_id}")
    suspend fun getNanda(@Path("nandas_id") nandasId: Long): Response<NandaResponse>
}
