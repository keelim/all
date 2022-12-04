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
package com.keelim.nandadiagnosis.ui.main.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keelim.nandadiagnosis.data.entity.DiagnosisItem
import com.keelim.nandadiagnosis.databinding.ItemListviewBinding

class DiagnosisRecyclerViewAdapter(
    var listener: (Int) -> Unit,
) : ListAdapter<DiagnosisItem, DiagnosisRecyclerViewAdapter.ViewHolder>(
    diffUtil
) {

    inner class ViewHolder(private val binding: ItemListviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DiagnosisItem, position: Int) = with(binding) {
            diagnosisItem.text = item.diagnosis
            diagnosisDes.text = item.diagnosis_description

            root.setOnClickListener {
                listener.invoke(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position], position)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<DiagnosisItem>() {
            override fun areItemsTheSame(oldItem: DiagnosisItem, newItem: DiagnosisItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DiagnosisItem, newItem: DiagnosisItem): Boolean {
                return oldItem.diagnosis == newItem.diagnosis
            }
        }
    }
}
