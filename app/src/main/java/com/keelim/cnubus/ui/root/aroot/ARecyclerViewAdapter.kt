package com.keelim.cnubus.ui.root.aroot

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keelim.cnubus.databinding.ItemListBinding

class ARecyclerViewAdapter(private val values: Array<String>) : RecyclerView.Adapter<ARecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false), listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item
    }

    override fun getItemCount(): Int = values.size

    interface OnRootClickListener {
        fun onRootClickListener(position: Int)
    }

    var listener: OnRootClickListener? = null

    inner class ViewHolder(binding: ItemListBinding, listener: OnRootClickListener?) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.text

        init {
            binding.root.setOnClickListener {
                listener?.onRootClickListener(adapterPosition)
            }
        }
    }

}