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
import androidx.recyclerview.widget.RecyclerView
import com.keelim.cnubus.data.model.Station
import com.keelim.cnubus.databinding.ItemStationBinding
import com.keelim.cnubus.ui.custom.Badge
import com.keelim.cnubus.utils.dip

class StationsAdapter : RecyclerView.Adapter<StationsAdapter.ViewHolder>() {

    var data: List<Station> = emptyList()

    var onItemClickListener: ((Station) -> Unit)? = null
    var onFavoriteClickListener: ((Station) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemStationBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding: ItemStationBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(data[adapterPosition])
            }

            binding.favorite.setOnClickListener {
                onFavoriteClickListener?.invoke(data[adapterPosition])
            }
        }

        fun bind(station: Station) {
            binding.badgeContainer.removeAllViews()

            station.connectedSubways
                .forEach { subway ->
                    binding.badgeContainer.addView(
                        Badge(binding.root.context).apply {
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
            binding.stationNameTextView.text = station.name
            binding.favorite.isChecked = station.isFavorited
        }
    }
}
