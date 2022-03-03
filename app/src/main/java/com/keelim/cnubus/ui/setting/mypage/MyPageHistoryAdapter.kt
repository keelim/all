package com.keelim.cnubus.ui.setting.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.keelim.cnubus.R
import com.keelim.cnubus.data.db.entity.History
import com.keelim.cnubus.databinding.ItemHistoryBinding

class MyPageHistoryAdapter(
    val itemDelete: (History) -> Unit
) :
    ListAdapter<History, MyPageHistoryAdapter.HistoryViewHolder>(diffUtil) {
    inner class HistoryViewHolder(val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: History) {
            binding.ivHistoryView.load(R.mipmap.ic_launcher_round) {
                crossfade(true)
                placeholder(R.mipmap.ic_launcher_round)
                transformations(CircleCropTransformation())
            }
            binding.tvHistoryView.text = item.destination
            binding.btnDelete.setOnClickListener {
                itemDelete.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            ItemHistoryBinding.inflate(
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
        val diffUtil = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.uid == newItem.uid
            }
        }
    }
}