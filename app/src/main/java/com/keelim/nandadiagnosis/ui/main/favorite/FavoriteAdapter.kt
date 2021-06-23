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
package com.keelim.nandadiagnosis.ui.main.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keelim.nandadiagnosis.data.db.entity.NandaEntity
import com.keelim.nandadiagnosis.databinding.ItemFavoriteBinding

class FavoriteAdapter : ListAdapter<NandaEntity, FavoriteAdapter.ViewHolder>(diffUtil) {
  inner class ViewHolder(val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: NandaEntity) {
      binding.diagnosisItem.text = item.diagnosis
      binding.diagnosisDes.text = item.reason
      binding.className.text = item.class_name
      binding.domainName.text = item.domain_name
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      ItemFavoriteBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
      )
    )
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    return holder.bind(currentList[position])
  }

  companion object {
    val diffUtil = object : DiffUtil.ItemCallback<NandaEntity>() {
      override fun areItemsTheSame(oldItem: NandaEntity, newItem: NandaEntity): Boolean {
        return oldItem == newItem
      }

      override fun areContentsTheSame(oldItem: NandaEntity, newItem: NandaEntity): Boolean {
        return oldItem == newItem
      }
    }
  }
}
