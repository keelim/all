/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
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
package com.keelim.cnubus.ui.screen.setting.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.ItemHistoryBinding
import com.keelim.data.db.entity.CnuHistory

class MyPageHistoryAdapter(
    val itemDelete: (CnuHistory) -> Unit
) :
    ListAdapter<CnuHistory, MyPageHistoryAdapter.HistoryViewHolder>(diffUtil) {
    inner class HistoryViewHolder(val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CnuHistory) {
            binding.run {
                ivHistoryView.load(R.mipmap.ic_launcher_round) {
                    crossfade(true)
                    placeholder(R.mipmap.ic_launcher_round)
                    transformations(CircleCropTransformation())
                }
                tvHistoryView.text = item.destination
                val root = "Root"
                tvRootView.text = root + item.root
                btnDelete.setOnClickListener {
                    itemDelete.invoke(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CnuHistory>() {
            override fun areItemsTheSame(oldItem: CnuHistory, newItem: CnuHistory): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: CnuHistory, newItem: CnuHistory): Boolean {
                return oldItem.uid == newItem.uid
            }
        }
    }
}
