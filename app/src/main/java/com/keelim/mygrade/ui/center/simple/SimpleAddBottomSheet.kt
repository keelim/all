package com.keelim.mygrade.ui.center.simple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.keelim.common.extensions.toast
import com.keelim.mygrade.R
import com.keelim.mygrade.databinding.BottomsheetSimpleAddBinding

class SimpleAddBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomsheetSimpleAddBinding? = null
    private val binding get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = DataBindingUtil.inflate<BottomsheetSimpleAddBinding>(
        inflater, R.layout.bottomsheet_simple_add, container, false
    ).apply {
        buttonConfirm.setOnClickListener {
            toast("현재 기능 추가를 위해 준비 중입니다.")
            dismiss()
        }
    }.also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
