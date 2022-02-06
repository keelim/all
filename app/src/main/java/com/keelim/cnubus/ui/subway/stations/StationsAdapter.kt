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
package com.keelim.cnubus.ui.subway.stations

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keelim.cnubus.data.model.Station
import com.keelim.cnubus.databinding.ItemStationBinding
import com.keelim.cnubus.ui.custom.Badge
import com.keelim.common.extensions.dip

class StationsAdapter(
    var onItemClickListener: ((Station) -> Unit)? = null,
    var onFavoriteClickListener: ((Station) -> Unit)? = null
) : ListAdapter<Station, StationsAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemStationBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(currentList[position])


    inner class ViewHolder(private val binding: ItemStationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(station: Station) = with(binding){
            root.setOnClickListener {
                onItemClickListener?.invoke(station)
            }

            favorite.setOnClickListener {
                onFavoriteClickListener?.invoke(station)
            }

            badgeContainer.removeAllViews()

            station.connectedSubways
                .forEach { subway ->
                    badgeContainer.addView(
                        Badge(root.context).apply {
                            badgeColor = subway.color
                            text = subway.label
                            layoutParams =
                                LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                ).apply {
                                    rightMargin = dip(6f)
                                }
                        }
                    )
                }
            stationNameTextView.text = station.name
            favorite.isChecked = station.isFavorited
        }
    }

    companion object{
        val diffUtil = object: DiffUtil.ItemCallback<Station>(){
            override fun areItemsTheSame(oldItem: Station, newItem: Station): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Station, newItem: Station): Boolean {
                return oldItem == newItem
            }
        }
    }
}
