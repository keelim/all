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
package com.keelim.comssa.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keelim.comssa.data.db.entity.Search
import com.keelim.comssa.databinding.ItemFooterBinding
import com.keelim.comssa.databinding.ItemHeaderBinding
import com.keelim.comssa.databinding.ItemSearchBinding

class FavoriteAdapter(
  private val click: (Int, Int) -> Unit,
) : ListAdapter<Search, RecyclerView.ViewHolder>(diffUtil) {
  inner class ViewHolder(private val binding: ItemSearchBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Search) = with(binding) {
      title.text = item.title
      description.text = item.description
      favoriteSwitch.isChecked = item.favorite == 1
      binding.favoriteSwitch.setOnClickListener {
        click.invoke(item.favorite, item.id)
      }
    }
  }

  inner class HeaderViewHolder(private val binding: ItemHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind() {
    }
  }

  inner class FooterViewHolder(private val binding: ItemFooterBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind() {
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (viewType) {
      TYPE_HEADER -> HeaderViewHolder(ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
      TYPE_ITEM -> FooterViewHolder(ItemFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
      else -> ViewHolder(ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    if (holder is ViewHolder) {
      holder.bind(currentList[position])
    }
  }

  companion object {
    val diffUtil = object : DiffUtil.ItemCallback<Search>() {
      override fun areItemsTheSame(oldItem: Search, newItem: Search): Boolean {
        return newItem == oldItem
      }

      override fun areContentsTheSame(oldItem: Search, newItem: Search): Boolean {
        return newItem == oldItem
      }
    }

    const val TYPE_HEADER = 0
    const val TYPE_ITEM = 1
    const val TYPE_FOOTER = 2
  }
}
