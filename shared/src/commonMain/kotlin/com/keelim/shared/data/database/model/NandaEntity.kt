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
@file:OptIn(ExperimentalObjCName::class)

package com.keelim.shared.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@Entity(tableName = "nanda")
@ObjCName("nanda")
data class NandaEntity(
    @PrimaryKey val nanda_id: Int,
    val reason: String,
    val diagnosis: String,
    val class_name: String,
    val domain_name: String,
    val category: Int,
    val favorite: Int,
) {
    override fun toString(): String {
        return "$nanda_id \n $reason \n $diagnosis \n $class_name \n $domain_name \n $category"
    }
}
