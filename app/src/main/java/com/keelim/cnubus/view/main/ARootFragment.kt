package com.keelim.cnubus.view.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.keelim.cnubus.R
import com.keelim.cnubus.view.MapsActivity
import kotlinx.android.synthetic.main.fragment_a_root.view.*

class ARootFragment : Fragment(R.layout.fragment_a_root) {
    private lateinit var rootList: Array<String>
    private lateinit var intentList: Array<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_a_root, container, false)

        rootList = resources.getStringArray(R.array.aroot)
        intentList = resources.getStringArray(R.array.a_intent_array)
        applyList(root, rootList)

        root.lv_aroot.setOnItemClickListener { adapterView, view, i, l ->
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
        root.lv_aroot.adapter = adapter
    }
}