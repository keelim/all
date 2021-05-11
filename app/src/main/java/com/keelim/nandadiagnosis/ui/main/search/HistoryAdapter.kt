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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keelim.nandadiagnosis.data.db.history.History
import com.keelim.nandadiagnosis.databinding.ItemHistoryBinding

class HistoryAdapter(val historyDeleteClickListener: (String) -> Unit) : ListAdapter<History, HistoryAdapter.HistoryItemViewHolder>(diffUtil) {
  inner class HistoryItemViewHolder(private val binding: ItemHistoryBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(historyModel: History) {
      binding.historyKeywordTextView.text = historyModel.keyword
      binding.historyKeywordDeleteButton.setOnClickListener {
        historyModel.keyword.orEmpty()
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
    return HistoryItemViewHolder(
      ItemHistoryBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
      )
    )
  }

  override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
    holder.bind(currentList[position])
  }

  companion object {
    val diffUtil = object : DiffUtil.ItemCallback<History>() {
      override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
        return oldItem == newItem
      }

      override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
        return oldItem.keyword == newItem.keyword
      }
    }
  }
}
