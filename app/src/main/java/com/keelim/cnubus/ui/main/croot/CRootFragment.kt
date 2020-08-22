package com.keelim.cnubus.ui.main.croot

import android.os.Bundle
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
            Snackbar.make(crootLayout, "C 노선 지도 업데이트 준비 중입니다. ", Snackbar.LENGTH_LONG).show()
        }

        rootList = resources.getStringArray(R.array.croot)
        requireView().lv_croot.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, rootList)

    }
}