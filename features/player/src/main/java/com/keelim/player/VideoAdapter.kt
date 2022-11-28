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
package com.keelim.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.keelim.nandadiagnosis.data.model.Video
import com.keelim.player.databinding.ItemVideoBinding

class VideoAdapter(
    private val click: (String, String) -> Unit
) : ListAdapter<Video, VideoAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Video) = with(binding) {
            titleTextView.text = item.title
            subTitleTextView.text = item.subtitle
            thumbnailImageView.load(item.thumb) {
                crossfade(true)
            }
            root.setOnClickListener {
                click.invoke(item.sources, item.title)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem == newItem
            }
        }
    }
}
