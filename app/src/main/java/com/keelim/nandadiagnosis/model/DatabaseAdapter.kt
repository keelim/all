package com.keelim.nandadiagnosis.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.model.roomdb.NandaEntity

class DatabaseAdapter(private val context: Context, private val arrayList: List<NandaEntity>) : BaseAdapter() {
    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(i: Int): Any {
        return arrayList[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }


    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_db, null)
        view.findViewById<TextView>(R.id.db_diagnosis).text = arrayList[i].diagnosis
        return view
    }


}