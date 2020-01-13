package com.keelim.cnubus.activities.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.keelim.cnubus.R

class CRootFragment : Fragment() {

    private lateinit var rootList: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_c_root, container, false)
        root.findViewById<ListView>(R.id.lv_croot)
            .setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                Toast.makeText(
                    activity,
                    rootList[position] + "기능 준비 중입니다. 잠시만 기다려 주세요",
                    Toast.LENGTH_SHORT
                ).show()
            }
        rootList = resources.getStringArray(R.array.croot)
        applyList(root, rootList)
        return root
    }

    private fun applyList(root: View, rootList: Array<String>) {
        val adapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, rootList)
        root.findViewById<ListView>(R.id.lv_croot).adapter = adapter
    }
}