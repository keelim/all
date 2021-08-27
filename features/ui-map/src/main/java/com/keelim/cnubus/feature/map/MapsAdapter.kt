package com.keelim.cnubus.feature.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keelim.cnubus.data.model.gps.LocationList
import com.keelim.cnubus.feature.map.databinding.ItemMarkerBinding
import timber.log.Timber


class MapsAdapter(
    private val click: (Int) -> Unit,
) : ListAdapter<LocationList, MapsAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemMarkerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LocationList, position: Int) {
            binding.floatingMarker.text = position.toString()
            click.invoke(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMarkerBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position], position)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<LocationList>() {
            override fun areItemsTheSame(oldItem: LocationList, newItem: LocationList): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: LocationList, newItem: LocationList): Boolean {
                return oldItem == newItem
            }
        }
    }
}