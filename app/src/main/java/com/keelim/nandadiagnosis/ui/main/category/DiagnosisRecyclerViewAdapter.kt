package com.keelim.nandadiagnosis.ui.main.category

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keelim.nandadiagnosis.databinding.ItemListviewBinding
import com.keelim.nandadiagnosis.ui.WebActivity


class DiagnosisRecyclerViewAdapter(private val values: List<DiagnosisItem>, private val nav: Int): RecyclerView.Adapter<DiagnosisRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListviewBinding.inflate(LayoutInflater.from(parent.context), parent, false), nav)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.diagnosisItem.text = item.item
        holder.diagnosisDes.text = item.description
    }

    override fun getItemCount(): Int {
        return values.size
    }

    inner class ViewHolder(val binding: ItemListviewBinding, private val nav:Int):RecyclerView.ViewHolder(binding.root){
        val diagnosisItem:TextView = binding.diagnosisItem
        val diagnosisDes : TextView = binding.diagnosisDes
        init {
            binding.root.setOnClickListener {
                val pos = adapterPosition
                val total = nav+pos+1
                Intent(it.context, WebActivity::class.java).apply {
                    putExtra("URL", "https://keelim.github.io/nandaDiagnosis/$total.html")
                    it.context.startActivity(this)
                }
            }
        }
    }

    class DiagnosisItem(var item: String, var description: String)
}