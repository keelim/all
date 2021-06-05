package com.keelim.nandadiagnosis.ui.reference_search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.keelim.nandadiagnosis.databinding.ItemReferenceBinding
import com.keelim.reference_search.Reference

class ReferenceAdapter :
    ListAdapter<Reference, ReferenceAdapter.ReferenceItemViewHolder>(diffUtil) {
    inner class ReferenceItemViewHolder(private val binding: ItemReferenceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reference: Reference) {
            binding.titleTextView.text = reference.title
            binding.description.text = reference.description
            binding.imageView.load(reference.image)

            Glide.with(binding.imageView.context)
                .load(reference.image)
                .into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReferenceItemViewHolder {
        return ReferenceItemViewHolder(
            ItemReferenceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReferenceItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Reference>() {
            override fun areItemsTheSame(oldItem: Reference, newItem: Reference): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Reference, newItem: Reference): Boolean {
                return oldItem.title == newItem.title
            }
        }
    }
}