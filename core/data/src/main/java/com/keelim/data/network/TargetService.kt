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
package com.keelim.data.network

import com.keelim.data.model.Books
import com.keelim.data.model.entity.NandaEntity2
import com.keelim.data.model.NandaResponse
import com.keelim.data.model.NandasResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

sealed interface TargetService {
    interface NandaService : TargetService {
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

    interface CnubusService : TargetService
    interface ComssaService : TargetService
    interface MyGradeService : TargetService {
        @GET("books/v1/volumes")
        suspend fun getBooks(
            @Query("q") query: String,
            @Query("startIndex") startIndex: Int,
            @Query("maxResults") limit: Int
        ): Books

    }

    interface YrService : TargetService
}
