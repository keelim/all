package com.keelim.cnubus.ui.root.night

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.keelim.cnubus.databinding.FragmentItemBinding

class NightRecyclerViewAdapter(private val values: List<String>) : RecyclerView.Adapter<NightRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber

        init {
            binding.root.setOnClickListener {
                Snackbar.make(it, "업데이트 중입니다. ", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}