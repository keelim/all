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
package com.keelim.cnubus.ui.stationarrivals

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keelim.cnubus.databinding.ItemArrivalBinding
import com.keelim.data.model.ArrivalInformation

class StationArrivalsAdapter : RecyclerView.Adapter<StationArrivalsAdapter.ViewHolder>() {

    var data: List<ArrivalInformation> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemArrivalBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val viewBinding: ItemArrivalBinding) : RecyclerView.ViewHolder(viewBinding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(arrival: ArrivalInformation) {
            viewBinding.labelTextView.badgeColor = arrival.subway.color
            viewBinding.labelTextView.text = "${arrival.subway.label} - ${arrival.direction}"
            viewBinding.destinationTextView.text = "🚩 ${arrival.destination}"
            viewBinding.arrivalMessageTextView.text = arrival.message
            viewBinding.arrivalMessageTextView.setTextColor(if (arrival.message.contains("당역")) Color.RED else Color.DKGRAY)
            viewBinding.updatedTimeTextView.text = "측정 시간: ${arrival.updatedAt}"
        }
    }
}
