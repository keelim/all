package com.keelim.cnubus.feature.map.ui.map3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.keelim.cnubus.data.model.maps.House
import com.keelim.cnubus.feature.map.databinding.ItemHouseDetailForViewpagerBinding

class LocationPagerAdapter(
    val itemClicked: (House) -> Unit
) : ListAdapter<House, LocationPagerAdapter.ItemViewHolder>(diffUtil) {

    inner class ItemViewHolder(val binding: ItemHouseDetailForViewpagerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: House)  = with(binding){
            titleTextView.text = item.title
            priceTextView.text = item.price

            root.setOnClickListener {
                itemClicked(item)
            }
            thumbnailIamgeView.load(item.imgUrl){
                crossfade(true)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemHouseDetailForViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<House>() {
            override fun areItemsTheSame(oldItem: House, newItem: House): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: House, newItem: House): Boolean {
                return oldItem == newItem
            }

        }
    }
}