package com.keelim.nandadiagnosis.ui.main.category

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keelim.nandadiagnosis.databinding.ItemListviewBinding
import com.keelim.nandadiagnosis.model.DiagnosisItem
import java.util.ArrayList


class DiagnosisRecyclerViewAdapter : RecyclerView.Adapter<DiagnosisRecyclerViewAdapter.ViewHolder>() {
    var list: List<DiagnosisItem> = listOf()

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListviewBinding.inflate(LayoutInflater.from(parent.context), parent, false), listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.itemTextView.text = item.diagnosis
        holder.desView.text = item.diagnosis_description
    }

    override fun getItemCount(): Int = list.size

    interface OnSearchItemClickListener {
        fun onSearchItemClick(position: Int)
    }

    fun setDiagnosisItem(items: List<DiagnosisItem>) {
        list = items
    }

    var listener: OnSearchItemClickListener? = null

    inner class ViewHolder(binding: ItemListviewBinding, listener: OnSearchItemClickListener?) :
            RecyclerView.ViewHolder(binding.root) {
        val itemTextView: TextView = binding.diagnosisItem
        val desView: TextView = binding.diagnosisDes

        init {
            binding.root.setOnClickListener {
                listener?.onSearchItemClick(absoluteAdapterPosition)
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + desView.text + "'"
        }
    }
}

