package com.keelim.cnubus.ui.main.broot

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.keelim.cnubus.R
import com.keelim.cnubus.ui.MapsActivity
import kotlinx.android.synthetic.main.fragment_b_root.view.*

class BRootFragment : Fragment(R.layout.fragment_b_root) {
    private lateinit var rootList: Array<String>
    private lateinit var intentList: Array<String>


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rootList = resources.getStringArray(R.array.broot)
        intentList = resources.getStringArray(R.array.b_intent_array)
        requireView().lv_broot.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, rootList)

        requireView().lv_broot.setOnItemClickListener { _, _, i, _ ->
            Toast.makeText(activity, rootList[i] + "정류장 입니다.", Toast.LENGTH_SHORT).show()

            Intent(activity, MapsActivity::class.java).apply {
                putExtra("location", intentList[i])
                startActivity(this)
            }
        }
    }
}