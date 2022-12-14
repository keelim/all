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
package com.keelim.nandadiagnosis.ui.main.search.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keelim.data.db.entity.NandaHistory
import com.keelim.nandadiagnosis.databinding.ItemHistoryBinding

class HistoryAdapter(
    val historyDeleteListener: (String) -> Unit,
    val textSelectListener: (String) -> Unit,
) :
    ListAdapter<NandaHistory, HistoryAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NandaHistory) = with(binding) {
            historyKeywordTv.text = item.keyword
            historyDeleteButton.setOnClickListener {
                historyDeleteListener(item.keyword.orEmpty())
            }

            historyKeywordTv.setOnClickListener {
                textSelectListener(item.keyword.orEmpty())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<NandaHistory>() {
            override fun areItemsTheSame(oldItem: NandaHistory, newItem: NandaHistory): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: NandaHistory, newItem: NandaHistory): Boolean {
                return oldItem.keyword == newItem.keyword
            }
        }
    }
}
