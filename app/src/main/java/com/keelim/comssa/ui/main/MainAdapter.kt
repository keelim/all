package com.keelim.comssa.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keelim.comssa.data.db.entity.Search
import com.keelim.comssa.databinding.ItemSearchBinding

class MainAdapter(
    private val favoriteListener: (Int, Int) -> Unit,
): ListAdapter<Search, MainAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemSearchBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item:Search) = with(binding){
            title.text = item.title
            description.text = item.description
            favoriteSwitch.isChecked = item.favorite == 1
            favoriteSwitch.setOnClickListener {
                favoriteListener.invoke(item.favorite, item.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        val diffUtil = object :DiffUtil.ItemCallback<Search>(){
            override fun areItemsTheSame(oldItem: Search, newItem: Search): Boolean {
                return newItem == oldItem
            }

            override fun areContentsTheSame(oldItem: Search, newItem: Search): Boolean {
                return newItem == oldItem
            }
        }
    }
}