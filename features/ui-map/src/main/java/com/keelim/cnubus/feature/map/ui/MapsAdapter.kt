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
package com.keelim.cnubus.feature.map.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keelim.cnubus.data.model.gps.LocationList
import com.keelim.cnubus.feature.map.databinding.ItemMarkerBinding

class MapsAdapter(
    private val click: (Int) -> Unit,
) : ListAdapter<LocationList, MapsAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemMarkerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LocationList, position: Int) {
            binding.floatingMarker.text = position.toString()
            click.invoke(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMarkerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position], position)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<LocationList>() {
            override fun areItemsTheSame(oldItem: LocationList, newItem: LocationList): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: LocationList, newItem: LocationList): Boolean {
                return oldItem == newItem
            }
        }
    }
}
