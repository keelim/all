package com.keelim.mygrade.ui.center.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keelim.common.extensions.ViewHolderLifecycleInitializer
import com.keelim.data.db.entity.SimpleHistory
import com.keelim.mygrade.databinding.ItemNotificationBinding

class HistoryAdapter : ListAdapter<SimpleHistory, HistoryViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SimpleHistory>() {
            override fun areItemsTheSame(oldItem: SimpleHistory, newItem: SimpleHistory): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: SimpleHistory,
                newItem: SimpleHistory,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class HistoryViewHolder(val binding: ItemNotificationBinding) :
    RecyclerView.ViewHolder(binding.root), ViewHolderLifecycleInitializer {
    override var lifecycleOwner: LifecycleOwner? = null
    init {
        initialize(itemView)
    }
    fun bind(item: SimpleHistory) = with(binding) {
        title.text = item.name
        date.text = item.date
        version.text = "version: ${item.grade}"
    }
}
