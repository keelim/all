package com.keelim.mygrade.ui.screen.center.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.keelim.composeutil.setThemeContent
import com.keelim.data.model.GradeResult
import com.keelim.mygrade.R
import com.keelim.mygrade.utils.Keys
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SimpleAddBottomSheet : BottomSheetDialogFragment() {
    private val viewModel by viewModels<QuickAddViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = setThemeContent {
        QuickAddRoute()
    }.also {
        initFlow()
    }

    private fun initFlow() {
        viewModel.quickAddUiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it.normalProbability.value != 0 && it.student != 0) {
                    findNavController().navigate(
                        R.id.gradeFragment,
                        bundleOf(
                            Keys.MAIN_TO_GRADE to GradeResult(
                                it.normalProbability.grade(),
                                Level((it.normalProbability.value * it.student) / 100).toProcess(it.student.toString())
                            ),
                        )
                    )
                    viewModel.clear()
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}
