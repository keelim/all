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
import com.keelim.common.extensions.toast
import com.keelim.data.db.entity.History
import com.keelim.data.model.Result
import com.keelim.data.repository.IoRepository
import com.keelim.mygrade.R
import com.keelim.mygrade.databinding.FragmentMainBinding
import com.keelim.mygrade.utils.Keys
import com.keelim.mygrade.utils.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.apache.commons.math3.distribution.NormalDistribution

@HiltViewModel
class MainViewModel @Inject constructor(
    private val ioRepository: IoRepository,
) : ViewModel() {
    private val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState.UnInitialized)
    val state: StateFlow<MainState> = _state.asStateFlow()

    fun submit(origin: Float, average: Float, number: Float, student: Int, flag: Boolean = false) =
        viewModelScope.launch {
            _state.emit(MainState.Loading)
            if (flag.not()) return@launch
            runCatching {
                true
            }.onSuccess {
                _state.emit(
                    MainState.Success(
                        it,
                        getNormalProbabilityAtZ(((origin - average) / number).toDouble()),
                        student
                    )
                )
            }.onFailure {
                _state.emit(MainState.Error("??????"))
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
        ioRepository.insertHistories(
            History(
                Date().time.toString(),
                origin.toInt(),
                average,
                number,
                student,
                grade.toFloat(),
                level
            )
        )
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
                viewModel.submit(
                    valueOrigin.text.toString().toFloat(),
                    valueAverage.text.toString().toFloat(),
                    valueNumber.text.toString().toFloat(),
                    valueStudent.text.toString().toInt(),
                    true
                )
            }
            floatingActionButton.setOnClickListener {
                findNavController().navigate(R.id.simpleAddBottomSheet)
            }
            bottomAppBar.setNavigationOnClickListener {
                toast("?????? ?????? ?????? ????????????.")
                startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
            }
            bottomAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_notification -> {
                        findNavController().navigate(R.id.notificationFragment)
                        true
                    }
                    R.id.menu_history -> {
                        findNavController().navigate(R.id.historyFragment)
                        true
                    }
                    else -> false
                }
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
                        requireContext().snack(binding.root, "????????? ??????????????????")
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
                            findNavController().navigate(
                                R.id.gradeFragment,
                                Bundle().apply {
                                    putParcelable(
                                        Keys.MAIN_TO_GRADE,
                                        Result(
                                            grade,
                                            getLevel(
                                                (
                                                    it.value * binding.valueStudent.text.toString()
                                                        .toInt()
                                                    ) / 100
                                            )
                                        )
                                    )
                                }
                            )
                        }
                    }
                    is MainState.Error -> requireContext().snack(binding.root, "????????? ??????????????????")
                }
            }.launchIn(lifecycleScope)
    }

    private fun getLevel(level: Int): String = level.toString() + " / " + binding.valueStudent.text

    private fun validation(): Boolean = with(binding) {
        var flag = true
        if (valueOrigin.text.toString().isEmpty()) {
            valueOrigin.error = "??? ????????? ??????????????????"
            flag = false
        }
        if (valueAverage.text.toString().isEmpty()) {
            valueAverage.error = "?????? ?????? ??????????????????"
            flag = false
        }
        if (valueNumber.text.toString().isEmpty()) {
            valueNumber.error = "?????? ????????? ??????????????????"
            flag = false
        }
        if (valueStudent.text.toString().isEmpty()) {
            valueStudent.error = "?????? ?????? ??????????????????"
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
        val temp: Int = 0,
    ) : MainState()

    data class Error(val message: String) : MainState()
}
