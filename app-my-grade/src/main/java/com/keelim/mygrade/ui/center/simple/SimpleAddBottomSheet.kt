package com.keelim.mygrade.ui.center.simple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.keelim.common.extensions.snack
import com.keelim.common.extensions.toast
import com.keelim.data.model.Result
import com.keelim.mygrade.R
import com.keelim.mygrade.databinding.BottomsheetSimpleAddBinding
import com.keelim.mygrade.ui.center.main.MainState
import com.keelim.mygrade.ui.center.main.MainViewModel
import com.keelim.mygrade.utils.Keys
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SimpleAddBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomsheetSimpleAddBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = DataBindingUtil.inflate<BottomsheetSimpleAddBinding>(
        inflater, R.layout.bottomsheet_simple_add, container, false
    ).apply {
        buttonConfirm.setOnClickListener {
            validate()
        }
    }.also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFlow()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun BottomsheetSimpleAddBinding.validate() {
        if (valueAverage.text.toString().count { it == ',' } != 3) {
            valueAverage.error = "올바른 양식으로 입력해주세요."
        } else {
            valueAverage.text.toString()
                .let { shortCut ->
                    val data = shortCut.split(",")
                    runCatching {
                        require(data.size == 4) {
                            "data length is always 4"
                        }
                        data
                    }.getOrElse {
                        emptyList()
                    }.let { items ->
                        if (items.isNotEmpty()) {
                            viewModel.submit(
                                items[0].toFloat(),
                                items[1].toFloat(),
                                items[2].toFloat(),
                                items[3].toInt(),
                                true
                            )
                        } else {
                            toast("알 수 없는 오류가 발생하였습니다. 다시 한번 시도해주세요")
                        }
                    }
                }
        }
    }

    private fun initFlow() = with(viewLifecycleOwner) {
        viewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is MainState.UnInitialized -> Unit
                    is MainState.Loading -> {
                        requireContext().snack(binding.root, "잠시만 기다려주세요")
                    }
                    is MainState.Success -> {
                        val grade = when {
                            it.value < 30 -> "A"
                            it.value < 60 -> "B"
                            it.value < 80 -> "C"
                            it.value < 100 -> "D"
                            else -> "F"
                        }

                        findNavController().navigate(
                            R.id.gradeFragment,
                            Bundle().apply {
                                putParcelable(
                                    Keys.MAIN_TO_GRADE,
                                    Result(
                                        grade,
                                        getLevel(
                                            (it.value * it.temp) / 100,
                                            it.temp
                                        )
                                    )
                                )
                            }
                        )
                    }
                    is MainState.Error -> requireContext().snack(binding.root, "오류가 발생했습니다")
                }
            }.launchIn(lifecycleScope)
    }

    private fun getLevel(level: Int, temp: Int): String = "$level / $temp"
}
