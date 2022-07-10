package com.keelim.mygrade.ui.center.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.keelim.common.extensions.snack
import com.keelim.data.db.entity.History
import com.keelim.data.model.Result
import com.keelim.data.repository.IoRepository
import com.keelim.mygrade.R
import com.keelim.mygrade.databinding.FragmentMainBinding
import com.keelim.mygrade.ui.GradeActivity
import com.keelim.mygrade.utils.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.apache.commons.math3.distribution.NormalDistribution

@HiltViewModel
class MainViewModel @Inject constructor(
    private val ioRepository: IoRepository,
) : ViewModel() {
    private val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState.UnInitialized)
    val state: StateFlow<MainState> = _state

    fun submit(origin: Float, average: Float, number: Float, student: Int, flag: Boolean = false) =
        viewModelScope.launch {
            _state.emit(MainState.Loading)
            if (flag.not()) return@launch
            runCatching {
                true
            }.onSuccess {
                _state.emit(MainState.Success(
                    it,
                    getNormalProbabilityAtZ(((origin - average) / number).toDouble()),
                ))
            }.onFailure {
                _state.emit(MainState.Error("실패"))
            }
        }

    private fun getNormalProbabilityAtZ(z: Double): Int {
        val normal = NormalDistribution()
        return if (z > 0) {
            100 - ((normal.probability(0.0, z) + 0.5) * 100).toInt()
        } else {
            ((normal.probability(0.0, -z) + 0.5) * 100).toInt()
        }
    }

    fun save(
        origin: Float,
        average: Float,
        number: Float,
        student: Int,
        grade: String,
        level: String,
    ) = viewModelScope.launch {
        ioRepository.insertHistories(History(Date().time.toString(),
            origin.toInt(),
            average,
            number,
            student,
            grade.toFloat(),
            level))
    }
}

@AndroidEntryPoint
class MainFragment : Fragment() {
    @Inject
    lateinit var themeManager: ThemeManager
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
                viewModel.submit(valueOrigin.text.toString().toFloat(),
                    valueAverage.text.toString().toFloat(),
                    valueNumber.text.toString().toFloat(),
                    valueStudent.text.toString().toInt(),
                    true)
            }
            notification.setOnClickListener { findNavController().navigate(R.id.notificationFragment) }
            history.setOnClickListener { findNavController().navigate(R.id.historyFragment) }
            footer.setOnLongClickListener {
                startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
                return@setOnLongClickListener true
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
                        if (validation()) {
                            val grade = when {
                                it.value < 30 -> "A"
                                it.value < 60 -> "B"
                                it.value < 80 -> "C"
                                it.value < 100 -> "D"
                                else -> "F"
                            }
                            startActivity(Intent(requireContext(),
                                GradeActivity::class.java).apply {
                                putExtra("data",
                                    Result(grade,
                                        getLevel((it.value * binding.valueStudent.text.toString()
                                            .toInt()) / 100)))
                            })
                        }
                    }
                    is MainState.Error -> requireContext().snack(binding.root, "오류가 발생했습니다")
                }
            }.launchIn(lifecycleScope)
    }

    private fun getLevel(level: Int): String = level.toString() + " / " + binding.valueStudent.text

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

sealed class MainState {
    object UnInitialized : MainState()
    object Loading : MainState()
    data class Success(
        val flag: Boolean,
        val value: Int,
    ) : MainState()
    data class Error(val message: String) : MainState()
}
