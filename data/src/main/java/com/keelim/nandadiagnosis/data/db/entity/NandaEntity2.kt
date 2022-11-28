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
package com.keelim.nandadiagnosis.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "nanda")
data class NandaEntity2(
    @PrimaryKey val nanda_id: Int,
    val reason: String,
    val diagnosis: String,
    val class_name: String,
    val domain_name: String,
    val category: Int,
    val favorite: Int,
    val created_at: Date,
) {
    override fun toString(): String {
        return "$nanda_id \n $reason \n $diagnosis \n $class_name \n $domain_name \n $category"
    }
}
