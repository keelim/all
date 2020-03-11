package com.keelim.cnubus.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.keelim.cnubus.R
import kotlinx.android.synthetic.main.fragment_c_root.*
import kotlinx.android.synthetic.main.fragment_c_root.view.*

class CRootFragment : Fragment() {

    private lateinit var rootList: Array<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_c_root, container, false)
        root.lv_croot.setOnItemClickListener { adapterView, view, i, l ->
            Snackbar.make(crootLayout, "기능 준비 중입니다.", Snackbar.LENGTH_LONG)
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