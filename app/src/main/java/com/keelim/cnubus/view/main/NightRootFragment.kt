package com.keelim.cnubus.view.main

import android.content.Intent
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
import com.keelim.cnubus.view.MapsActivity
import kotlinx.android.synthetic.main.fragment_night_root.*
import kotlinx.android.synthetic.main.fragment_night_root.view.*

class NightRootFragment : Fragment() {
    private lateinit var intentList: Array<String>
    private lateinit var rootList: Array<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_night_root, container, false)

        rootList = resources.getStringArray(R.array.night1)
        intentList = resources.getStringArray(R.array.night_intent_array)
        applyList(root, rootList)

        root.lv_nightroot.setOnItemClickListener { adapterView, view, i, l ->
            Snackbar.make(nightrootLayout, "기능 준비 중입니다. ", Snackbar.LENGTH_SHORT).show()

            Intent(activity, MapsActivity::class.java).apply {
                putExtra("location", intentList[i])
                startActivity(this)
            }
        }


        return root
    }

    private fun applyList(root: View, rootList: Array<String>) {
        val adapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, rootList)
        root.findViewById<ListView>(R.id.lv_nightroot).adapter = adapter
    }
}