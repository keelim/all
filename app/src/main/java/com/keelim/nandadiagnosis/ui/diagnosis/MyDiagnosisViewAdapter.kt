package com.keelim.nandadiagnosis.ui.diagnosis

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.keelim.nandadiagnosis.R
import kotlinx.android.synthetic.main.item_listview.view.*

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
        val findView = LayoutInflater.from(context).inflate(R.layout.item_listview, parent, false)

        findView.diagnosis_item.text = diagnosisItems!![position].diagnosis
        findView.diagnosis_des.text = diagnosisItems[position].diagnosis
        return findView
    }

    data class DiagnosisItem(var diagnosis: String, var diagnosis_description: String)
}