package com.keelim.cnubus.view.fragments

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
import kotlinx.android.synthetic.main.fragment_b_root.view.*

class BRootFragment : Fragment(R.layout.fragment_b_root) {
    private lateinit var rootList: Array<String>
    private lateinit var intentList: Array<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootList = resources.getStringArray(R.array.broot)
        intentList = resources.getStringArray(R.array.b_intent_array)
        view!!.lv_broot.adapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, rootList)

        view!!.lv_broot.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(activity, rootList[i] + "정류장 입니다.", Toast.LENGTH_SHORT).show()

            Intent(activity, MapsActivity::class.java).apply {
                putExtra("location", intentList[i])
                startActivity(this)
            }
        }

        return view
    }
}