package com.keelim.cnubus.ui.main.croot

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentCRootBinding

class CRootFragment : Fragment(R.layout.fragment_c_root) {
    private var fragmentCRootBinding: FragmentCRootBinding? = null
    private lateinit var rootList: Array<String>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCRootBinding.bind(view)
        fragmentCRootBinding = binding
        binding.lvCroot.setOnItemClickListener { _, _, _, _ ->
            Snackbar.make(binding.crootLayout, "C 노선 지도 업데이트 준비 중입니다. ", Snackbar.LENGTH_LONG).show()
        }
        rootList = resources.getStringArray(R.array.croot)
        binding.lvCroot.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, rootList)
    }

    override fun onDestroyView() {
        fragmentCRootBinding = null
        super.onDestroyView()
    }
}