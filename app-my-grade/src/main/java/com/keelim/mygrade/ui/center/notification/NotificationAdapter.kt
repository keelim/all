package com.keelim.mygrade.ui.center.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keelim.data.model.notification.Notification
import com.keelim.mygrade.databinding.ItemNotificationBinding

class NotificationAdapter : ListAdapter<Notification, NotificationViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Notification>() {
            override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class NotificationViewHolder(val binding: ItemNotificationBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Notification) = with(binding) {
        title.text = item.version
        description.text = item.desc
        date.text = item.desc
        version.text = "version: ${item.version}"
    }
}
