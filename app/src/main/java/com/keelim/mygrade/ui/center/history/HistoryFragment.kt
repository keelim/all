package com.keelim.mygrade.ui.center.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.keelim.common.extensions.repeatCallDefaultOnStarted


import com.keelim.common.extensions.toGone
import com.keelim.common.extensions.toVisible
import com.keelim.common.extensions.toast

import com.keelim.mygrade.databinding.FragmentHistoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment: Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val viewModel: HistoryViewModel by viewModels()
    private val binding get() = _binding!!
    private val historyAdapter by lazy {
        HistoryAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() = with(binding){
        historyRecycler.adapter = historyAdapter
    }

    private fun observeState() = viewLifecycleOwner.lifecycleScope.launch {
        repeatCallDefaultOnStarted {
            viewModel.state.collect {
                when(it){
                    is HistoryState.UnInitialized -> {}
                    is HistoryState.Loading -> {
                        binding.loading.toVisible()
                    }
                    is HistoryState.Success -> {
                        binding.loading.toGone()
                        if(it.data.isEmpty()){
                            binding.noHistory.toVisible()
                        } else{
                            binding.noHistory.toGone()
                            historyAdapter.submitList(it.data)
                        }
                    }
                    is HistoryState.Error -> {
                        binding.loading.toGone()
                        toast(it.message)
                    }
                }
            }
        }
    }
}
