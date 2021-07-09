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
package com.keelim.cnubus.ui.root.aroot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keelim.cnubus.databinding.ItemListBinding

class BRecyclerViewAdapter(
    var shortClickListener: (Int) -> Unit,
    var longClickListener: (Int) -> Unit,
) : ListAdapter<String, BRecyclerViewAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position], position)
    }

    inner class ViewHolder(private val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:String, position: Int) = with(binding){
            binding.text.text= item
            binding.root.setOnClickListener {
                shortClickListener.invoke(position)
            }

            binding.root.setOnLongClickListener {
                longClickListener.invoke(position)
                return@setOnLongClickListener true
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }
}
