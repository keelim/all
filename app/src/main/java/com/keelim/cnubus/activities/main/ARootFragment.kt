package com.keelim.cnubus.activities.main

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
import com.keelim.cnubus.activities.MapsActivity

class ARootFragment : Fragment() {
    private lateinit var rootList: Array<String>
    private lateinit var intentList: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_a_root, container, false)

        rootList = resources.getStringArray(R.array.aroot)
        intentList = resources.getStringArray(R.array.a_intent_array)
        applyList(root, rootList)

        root.findViewById<ListView>(R.id.lv_aroot).onItemClickListener =
            OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                Toast.makeText(
                    activity,
                    rootList[position] + "정류장 입니다.",
                    Toast.LENGTH_SHORT
                ).show()


                val intent = Intent(activity, MapsActivity::class.java)
                intent.putExtra("location", intentList[position])
                startActivity(intent)
            }

        return root
    }

    private fun applyList(root: View, rootList: Array<String>) {
        val adapter =
            ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, rootList)
        root.findViewById<ListView>(R.id.lv_aroot).adapter = adapter
    }
}