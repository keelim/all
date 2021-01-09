package com.keelim.cnubus.ui.root.croot

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

        rootList = resources.getStringArray(R.array.croot)

        binding.lvCroot.adapter = CRecyclerViewAdapter(rootList).apply {
            listener = object: CRecyclerViewAdapter.OnRootClickListener{
                override fun onRootClickListener(position: Int) {
                    Snackbar.make(binding.root, "C 노선 지도 업데이트 준비 중입니다. ", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        fragmentCRootBinding = null
        super.onDestroyView()
    }
}