package com.keelim.cnubus.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.keelim.cnubus.R
import kotlinx.android.synthetic.main.fragment_c_root.*
import kotlinx.android.synthetic.main.fragment_c_root.view.*

class CRootFragment : Fragment(R.layout.fragment_c_root) {

    private lateinit var rootList: Array<String>


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireView().lv_croot.setOnItemClickListener { _, _, _, _ ->
            Snackbar.make(crootLayout, "기능 준비 중입니다.", Snackbar.LENGTH_LONG)
        }

        rootList = resources.getStringArray(R.array.croot)
        requireView().lv_croot.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, rootList)

    }
}