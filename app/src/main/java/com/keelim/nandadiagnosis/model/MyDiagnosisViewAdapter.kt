package com.keelim.nandadiagnosis.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.keelim.nandadiagnosis.R

class MyDiagnosisViewAdapter(private val context: Context, private val diagnosisItems: ArrayList<DiagnosisItem>?) : BaseAdapter() {
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
        val find_view = LayoutInflater.from(context).inflate(R.layout.item_listview, null)
        val diagnosisItem = find_view.findViewById<TextView>(R.id.diagnosis_item)
        diagnosisItem.text = diagnosisItems!![position].diagnosis
        val diagnosisDes = find_view.findViewById<TextView>(R.id.diagnosis_des)
        diagnosisDes.text = diagnosisItems[position].diagnosis
        return find_view
    }

    data class DiagnosisItem(var diagnosis: String, var diagnosis_description: String)
}