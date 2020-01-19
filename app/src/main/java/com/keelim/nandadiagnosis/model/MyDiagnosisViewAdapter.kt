package com.keelim.nandadiagnosis.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.model.DiagnosisItem

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
        var view = LayoutInflater.from(context).inflate(R.layout.item_listview, null)
        val diagnosisItem = view.findViewById<TextView>(R.id.diagnosis_item)
        diagnosisItem.text = diagnosisItems!![position].diagnosis
        val diagnosisDes = view.findViewById<TextView>(R.id.diagnosis_des)
        diagnosisDes.text = diagnosisItems[position].diagnosis
        return view
    }

}