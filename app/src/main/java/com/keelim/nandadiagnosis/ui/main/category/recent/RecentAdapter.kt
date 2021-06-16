package com.keelim.nandadiagnosis.ui.main.category.recent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keelim.nandadiagnosis.data.model.Recent
import com.keelim.nandadiagnosis.databinding.ItemRecentBinding

class RecentAdapter(
    private val recents: List<Recent>
) : RecyclerView.Adapter<RecentAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRecentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Recent) {
            binding.itemCategory.text = item.category
            binding.itemClass.text = item.class_name
            binding.itemDomain.text = item.domain
            binding.itemReason.text = item.reason
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val actualPosition = position % recents.size
        holder.bind(recents[actualPosition])
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}