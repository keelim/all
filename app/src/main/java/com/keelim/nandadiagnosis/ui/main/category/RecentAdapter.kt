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
import androidx.recyclerview.widget.RecyclerView
import com.keelim.nandadiagnosis.data.entity.Recent
import com.keelim.nandadiagnosis.databinding.ItemRecentBinding

class RecentAdapter(private val recents: List<Recent>) : RecyclerView.Adapter<RecentAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRecentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Recent) = with(binding) {
            itemCategory.text = item.category
            itemClass.text = item.class_name
            itemDomain.text = item.domain
            itemReason.text = item.reason
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val actualPosition = position % recents.size
        holder.bind(recents[actualPosition])
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}
