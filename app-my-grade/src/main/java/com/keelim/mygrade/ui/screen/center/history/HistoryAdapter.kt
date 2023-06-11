package com.keelim.mygrade.ui.screen.center.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keelim.common.extensions.ViewHolderLifecycleInitializer
import com.keelim.data.source.local.History
import com.keelim.mygrade.databinding.ItemNotificationBinding

class HistoryAdapter : ListAdapter<History, HistoryViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: History,
                newItem: History,
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

    fun bind(item: History) = with(binding) {
        title.text = item.subject
        date.text = item.grade
        version.text = "version: ${item.grade}"
    }
}
