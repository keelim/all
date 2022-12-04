package com.keelim.mygrade.ui.center.history

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
import com.keelim.common.extensions.toGone
import com.keelim.common.extensions.toVisible
import com.keelim.common.extensions.toast
import com.keelim.data.db.entity.SimpleHistory
import com.keelim.data.repository.IoRepository
import com.keelim.mygrade.databinding.FragmentHistoryBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val ioRepository: IoRepository,
) : ViewModel() {
    val state: StateFlow<HistoryState> = ioRepository.simpleAll
        .map { items ->
            if (items.isEmpty()) {
                HistoryState.Loading
            } else {
                HistoryState.Success(items)
            }
        }
        .catch {
            HistoryState.Error("에러가 발생했습니다. 잠시 후 다시 시도해주세요.")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HistoryState.UnInitialized
        )
}

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val viewModel: HistoryViewModel by viewModels()
    private val binding get() = checkNotNull(_binding)
    private val historyAdapter = HistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentHistoryBinding.inflate(inflater, container, false)
        .apply {
            historyRecycler.adapter = historyAdapter
            imageviewBack.setOnClickListener {
                findNavController().navigateUp()
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
        viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
            when (it) {
                is HistoryState.UnInitialized -> Unit
                is HistoryState.Loading -> {
                    binding.loading.toVisible()
                }
                is HistoryState.Success -> {
                    binding.loading.toGone()
                    if (it.data.isEmpty()) {
                        binding.noHistory.toVisible()
                    } else {
                        binding.noHistory.toGone()
                        historyAdapter.submitList(it.data)
                    }
                }
                is HistoryState.Error -> {
                    binding.loading.toGone()
                    toast(it.message)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}

sealed class HistoryState {
    object UnInitialized : HistoryState()
    object Loading : HistoryState()
    data class Error(
        val message: String,
    ) : HistoryState()

    data class Success(
        val data: List<SimpleHistory>,
    ) : HistoryState()
}
