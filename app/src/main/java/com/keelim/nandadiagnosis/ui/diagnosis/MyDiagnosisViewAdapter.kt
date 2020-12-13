package com.keelim.nandadiagnosis.ui.diagnosis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.keelim.nandadiagnosis.databinding.ItemListviewBinding
import java.util.*

class MyDiagnosisViewAdapter(private val diagnosisItems: ArrayList<DiagnosisItem>?) : BaseAdapter() {

    override fun getCount(): Int {
        return diagnosisItems!!.size
    }

    override fun getItem(position: Int): Any {
        return diagnosisItems!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val binding: ItemListviewBinding = ItemListviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.diagnosisItem.text = diagnosisItems!![position].diagnosis
        binding.diagnosisDes.text = diagnosisItems[position].diagnosis

        return binding.root
    }

    data class DiagnosisItem(var diagnosis: String, var diagnosis_description: String)
}