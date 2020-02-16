package com.keelim.cnubus.model

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.keelim.cnubus.model.db.LocationEntity

class DatabaseAdapter(val context: Context, private val arrayList: List<LocationEntity>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val returnView = LayoutInflater.from(context).inflate(R.layout, null)
//        convertView.findViewById<R.id.>().text = arrayList[position].location1
        return convertView!!
    }

    override fun getItem(position: Int): Any = arrayList[position]



    override fun getItemId(position: Int): Long  = position.toLong()



    override fun getCount(): Int = arrayList.size


}