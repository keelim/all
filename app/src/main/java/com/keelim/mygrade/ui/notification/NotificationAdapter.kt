package com.keelim.mygrade.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keelim.mygrade.data.Release
import com.keelim.mygrade.databinding.ItemNotificationBinding

class NotificationAdapter(
) : ListAdapter<Release, NotificationAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Release) = with(binding) {
            title.text = item.title
            description.text = item.description
            date.text = item.date
            version.text = "version: ${item.version}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Release>() {
            override fun areItemsTheSame(oldItem: Release, newItem: Release): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Release, newItem: Release): Boolean {
                return oldItem == newItem
            }
        }
    }
}
