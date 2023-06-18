
package com.keelim.mygrade.ui.screen.center.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.keelim.common.extensions.snack
import com.keelim.common.extensions.toast
import com.keelim.data.model.GradeResult
import com.keelim.mygrade.R
import com.keelim.mygrade.databinding.FragmentMainBinding
import com.keelim.mygrade.utils.Keys
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.apache.commons.math3.distribution.NormalDistribution

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentMainBinding.inflate(inflater, container, false)
        .apply {
            btnSubmit.setOnClickListener {
                if (validation().not()) return@setOnClickListener
                viewModel.submit(
                    valueOrigin.text.toString().toFloat(),
                    valueAverage.text.toString().toFloat(),
                    valueNumber.text.toString().toFloat(),
                    valueStudent.text.toString().toInt(),
                    true,
                )
            }
            floatingActionButton.setOnClickListener {
                findNavController().navigate(R.id.simpleAddBottomSheet)
            }
            bottomAppBar.setNavigationOnClickListener {
                startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
            }
            bottomAppBar.setOnMenuItemClickListener { menuItem ->
                toast("현재 메뉴 준비 중입니다.")
                true
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

    private fun initFlow() {
        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is MainState.Error -> requireContext().snack(binding.root, "오류가 발생했습니다")
                    is MainState.Loading -> requireContext().snack(binding.root, "잠시만 기다려주세요")
                    is MainState.UnInitialized, MainState.Initialized -> Unit
                    is MainState.Success -> {
                        if (validation()) {
                            findNavController().navigate(
                                R.id.gradeFragment,
                                bundleOf(
                                    Keys.MAIN_TO_GRADE to GradeResult(
                                        it.value.grade(),
                                        Level(
                                            (it.value.value * binding.valueStudent.text.toString()
                                                .toInt()) / 100
                                        ).toProcess(binding.valueStudent.text.toString()),
                                    )
                                )
                            )
                            viewModel.moveToUnInitialized()
                        }
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun validation(): Boolean = with(binding) {
        var flag = true
        if (valueOrigin.text.toString().isEmpty()) {
            valueOrigin.error = "원 점수를 입력해주세요"
            flag = false
        }
        if (valueAverage.text.toString().isEmpty()) {
            valueAverage.error = "평균 값을 입력해주세요"
            flag = false
        }
        if (valueNumber.text.toString().isEmpty()) {
            valueNumber.error = "표준 편차를 입력해주세요"
            flag = false
        }
        if (valueStudent.text.toString().isEmpty()) {
            valueStudent.error = "학생 수를 입력해주세요"
            flag = false
        }
        return flag
    }
}

@JvmInline
value class Level(val level: Int)

internal fun Level.toProcess(count: String): String = "$level / $count"

@JvmInline
value class Zvalue(val z: Double)

internal fun Zvalue.getNormalProbability(): NormalProbability {
    return NormalDistribution()
        .let { normal ->
            if (z > 0) {
                NormalProbability(100 - ((normal.probability(0.0, z) + 0.5) * 100).toInt())
            } else {
                NormalProbability(((normal.probability(0.0, -z) + 0.5) * 100).toInt())
            }
        }
}

@JvmInline
value class NormalProbability(val value: Int)

internal fun NormalProbability.grade(isHardMode: Boolean = false): String {
    return when {
        value < 30 -> "A"
        value < 60 -> "B"
        value < 80 -> "C"
        value < 100 -> "D"
        else -> "F"
    }
}

sealed class MainState {
    object UnInitialized : MainState()
    object Loading : MainState()
    object Initialized : MainState()
    data class Success(
        val flag: Boolean,
        val value: NormalProbability,
        val temp: Int = 0,
    ) : MainState()

    data class Error(val message: String) : MainState()
}
