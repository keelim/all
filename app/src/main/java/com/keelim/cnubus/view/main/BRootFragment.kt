package com.keelim.cnubus.view.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.keelim.cnubus.R
import com.keelim.cnubus.view.MapsActivity
import kotlinx.android.synthetic.main.fragment_b_root.view.*

class BRootFragment : Fragment() {
    private lateinit var rootList: Array<String>
    private lateinit var intentList: Array<String>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_b_root, container, false)
        rootList = resources.getStringArray(R.array.broot)
        intentList = resources.getStringArray(R.array.b_intent_array)
        applyList(root, rootList)

        root.lv_broot.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(activity, rootList[i] + "정류장 입니다.", Toast.LENGTH_SHORT).show()

            Intent(activity, MapsActivity::class.java).apply {
                putExtra("location", intentList[i])
                startActivity(this)
            }
        }

        return root
    }

    private fun applyList(root: View, rootList: Array<String>) {
        val adapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, rootList)
        root.lv_broot.adapter = adapter
    }
}