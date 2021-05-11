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
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keelim.cnubus.databinding.ItemListBinding

class ARecyclerViewAdapter(private val values: Array<String>) : RecyclerView.Adapter<ARecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false), listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item
    }

    override fun getItemCount(): Int = values.size

    interface OnRootClickListener {
        fun onRootClickListener(position: Int)
        fun onRootLongClickListener(position: Int)
    }

    var listener: OnRootClickListener? = null

    inner class ViewHolder(binding: ItemListBinding, listener: OnRootClickListener?) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.text

        init {
            binding.root.setOnClickListener {
                listener?.onRootClickListener(bindingAdapterPosition)
            }

            binding.root.setOnLongClickListener {

                listener?.onRootLongClickListener(bindingAdapterPosition)
                return@setOnLongClickListener true
            }
        }
    }
}
