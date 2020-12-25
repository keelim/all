package com.keelim.nandadiagnosis.ui.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keelim.nandadiagnosis.data.db.NandaEntity
import com.keelim.nandadiagnosis.databinding.ItemListviewBinding



class SearchRecyclerViewAdapter(private val values: List<com.keelim.nandadiagnosis.data.db.NandaEntity>) : RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.itemTextView.text = item.diagnosis
        holder.desView.text = item.reason
        holder.classView.text = item.class_name
        holder.domainView.text = item.domain_name
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: ItemListviewBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemTextView: TextView = binding.diagnosisItem
        val desView: TextView = binding.diagnosisDes
        val classView = binding.className
        val domainView = binding.domainName

        init {
            binding.root.setOnClickListener {
                val pos = adapterPosition
                val db = values[pos]
                /*Snackbar.make(binding.root, "클래스 영역: ${db.class_name}도매인 영역${db.domain_name}", Snackbar.LENGTH_SHORT).show()*/
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + desView.text + "'"
        }
    }

}