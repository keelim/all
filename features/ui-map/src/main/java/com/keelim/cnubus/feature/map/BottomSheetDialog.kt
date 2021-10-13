package com.keelim.cnubus.feature.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.keelim.cnubus.feature.map.databinding.FragmentMapBottomSheetBinding

class BottomSheetDialog(
    private val map_title: String,
    private val map_description: String,
    private val map_position: String,
) : BottomSheetDialogFragment() {
    private var _binding: FragmentMapBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMapBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() = with(binding) {
        title.text = map_title
        description.text = map_description
        position.text = map_position
    }
}