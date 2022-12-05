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
import com.keelim.data.db.entity.NandaEntity
import com.keelim.nandadiagnosis.databinding.ItemListviewBinding

class SearchRecyclerViewAdapter2(
    val favoriteListener: (Int, Int) -> Unit,
) :
    ListAdapter<NandaEntity, SearchRecyclerViewAdapter2.ViewHolder>(diffUtil) {

    init {
        setHasStableIds(true) // 고유 id 를 설정
    }

    override fun getItemId(position: Int): Long = position.toLong()

    public override fun getItem(position: Int): NandaEntity = currentList[position]

    inner class ViewHolder(private val binding: ItemListviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NandaEntity, isActivated: Boolean = false) {
            itemView.isActivated = isActivated

            binding.diagnosisItem.text = item.diagnosis
            binding.diagnosisDes.text = item.reason
            binding.className.text = item.class_name
            binding.domainName.text = item.domain_name
            binding.searchSwitch.isChecked = item.favorite == 1

            binding.searchSwitch.setOnClickListener {
                favoriteListener(item.favorite, item.nanda_id)
            }
        }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<NandaEntity>() {
            override fun areItemsTheSame(oldItem: NandaEntity, newItem: NandaEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: NandaEntity, newItem: NandaEntity): Boolean {
                return oldItem.diagnosis == newItem.diagnosis
            }
        }
    }
}
