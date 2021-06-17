package com.keelim.nandadiagnosis.ui.main.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keelim.nandadiagnosis.data.db.NandaEntity
import com.keelim.nandadiagnosis.databinding.ItemFavoriteBinding

class FavoriteAdapter : ListAdapter<NandaEntity, FavoriteAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(
        val binding: ItemFavoriteBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NandaEntity) {
            binding.diagnosisItem.text = item.diagnosis
            binding.diagnosisDes.text = item.reason
            binding.className.text = item.class_name
            binding.domainName.text = item.domain_name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFavoriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<NandaEntity>() {
            override fun areItemsTheSame(oldItem: NandaEntity, newItem: NandaEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: NandaEntity, newItem: NandaEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}