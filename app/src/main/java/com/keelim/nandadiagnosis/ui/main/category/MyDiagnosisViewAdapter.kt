package com.keelim.nandadiagnosis.ui.main.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.keelim.nandadiagnosis.databinding.ItemListviewBinding
import com.keelim.nandadiagnosis.model.DiagnosisItem
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


}

