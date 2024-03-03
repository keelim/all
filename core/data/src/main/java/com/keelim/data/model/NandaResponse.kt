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
package com.keelim.data.model

import com.keelim.data.model.entity.NandaEntity2
import com.keelim.model.NandaResponse
import java.util.*

fun NandaResponse.toEntity() = NandaEntity2(
    nanda_id = id,
    reason = reason,
    diagnosis = diagnosis,
    class_name = class_name,
    domain_name = domain_name,
    category = category,
    favorite = favorite,
    created_at = Date(createdAt),
)
