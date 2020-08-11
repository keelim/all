package com.keelim.cnubus.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.keelim.cnubus.R
import com.keelim.cnubus.view.MapsActivity
import kotlinx.android.synthetic.main.fragment_night_root.*
import kotlinx.android.synthetic.main.fragment_night_root.view.*

class NightRootFragment : Fragment(R.layout.fragment_night_root) {
    private lateinit var intentList: Array<String>
    private lateinit var rootList: Array<String>


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rootList = resources.getStringArray(R.array.night1)
        intentList = resources.getStringArray(R.array.night_intent_array)


        requireView().lv_nightroot.setOnItemClickListener { _, _, i, _ ->
            Snackbar.make(nightrootLayout, "기능 준비 중입니다. ", Snackbar.LENGTH_SHORT).show()

            Intent(activity, MapsActivity::class.java).apply {
                putExtra("location", intentList[i])
                startActivity(this)
            }
        }

        requireView().lv_nightroot.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, rootList)

    }
}