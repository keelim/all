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
package com.keelim.nandadiagnosis.ui.main.search

import androidx.recyclerview.widget.DiffUtil
import com.keelim.nandadiagnosis.data.db.NandaEntity

class ListDiffCallback(private val oldTodoList: List<NandaEntity>, private val newTodoList: List<NandaEntity>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldTodoList.size
    override fun getNewListSize(): Int = newTodoList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTodoList[oldItemPosition].nanda_id == newTodoList[newItemPosition].nanda_id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newTodoList[newItemPosition] == oldTodoList[oldItemPosition]
    }
}
