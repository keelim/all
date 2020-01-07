package com.keelim.cnubus.activities.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.keelim.cnubus.R
import kotlinx.android.synthetic.main.fragment_night_root.*

class NightRootFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_night_root, container, false)

        lv_nightroot.setOnItemClickListener { parent, view, position, id ->
            view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorAccent))
        }
        applyList(resources.getStringArray(R.array.night1))

        Toast.makeText(context, "야간 노선 준비 중입니다.", Toast.LENGTH_SHORT).show()
        return root
    }

    private fun applyList(root: Array<String>) {
        val adapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, root)
        lv_nightroot.adapter = adapter
    }
}