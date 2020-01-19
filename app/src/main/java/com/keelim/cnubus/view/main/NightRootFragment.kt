package com.keelim.cnubus.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.keelim.cnubus.R

class NightRootFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =
            inflater.inflate(R.layout.fragment_night_root, container, false)

        root.findViewById<ListView>(R.id.lv_nightroot).adapter =
            ArrayAdapter(
                activity!!,
                android.R.layout.simple_list_item_1,
                resources.getStringArray(R.array.night1)
            )

        return root
    }
}