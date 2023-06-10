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
package com.keelim.map.screen.map3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.keelim.data.model.Location
import com.keelim.map.databinding.ItemHouseDetailForViewpagerBinding

class LocationPagerAdapter(
    val clicked: (Location) -> Unit,
    val longClicked: (Location) -> Unit,
) : ListAdapter<Location, LocationPagerAdapter.ItemViewHolder>(diffUtil) {

    inner class ItemViewHolder(val binding: ItemHouseDetailForViewpagerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Location) = with(binding) {
            titleTextView.text = item.name
            priceTextView.text = item.name

            root.setOnLongClickListener {
                longClicked(item)
                return@setOnLongClickListener true
            }
            root.setOnClickListener {
                clicked(item)
            }
            thumbnailIamgeView.load(item.imgUrl) {
                crossfade(true)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemHouseDetailForViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem.imgUrl == newItem.imgUrl
            }
        }
    }
}
