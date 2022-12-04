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
package com.keelim.cnubus.ui.root.basic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keelim.cnubus.data.model.gps.Location
import com.keelim.cnubus.databinding.ItemRootBinding

class RootAdapter(
    val click: (Int) -> Unit,
) :
    ListAdapter<Location, RootViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RootViewHolder(
        ItemRootBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: RootViewHolder, position: Int) {
        holder.bind(currentList[position], position, click)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(oldItem: Location, newItem: Location) = oldItem == newItem
            override fun areContentsTheSame(oldItem: Location, newItem: Location) =
                oldItem == newItem
        }
    }
}

class RootViewHolder(
    private val binding: ItemRootBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Location, position: Int, click: (Int) -> Unit) = with(binding) {
        data = item.name
        container.setOnClickListener { click.invoke(position) }
        executePendingBindings()
    }
}
