package com.keelim.mygrade.ui.center.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearSnapHelper
import com.keelim.mygrade.data.Release
import com.keelim.mygrade.databinding.FragmentNotificationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NotificationViewModel>()

    private val notificationAdapter by lazy {
        NotificationAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.fetchRelease()
        observeState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeState() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.state.collect {
                when (it) {
                    is NotificationState.UnInitialized -> {
                        binding.tvNoData.visibility = View.VISIBLE
                    }
                    is NotificationState.Loading -> Unit
                    is NotificationState.Success -> handleSuccess(it.data)
                    is NotificationState.Error -> handleError(it.message)
                }
            }
        }
    }

    private fun initViews() = with(binding) {
        val snapHelper = LinearSnapHelper()
        notificationRecycler.apply {
            adapter = notificationAdapter.apply {
                doOnNextLayout {
                }
            }
        }
        snapHelper.attachToRecyclerView(notificationRecycler)

    }

    private fun handleSuccess(data: List<Release>) {
        if (data.isEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE
        } else {
            binding.tvNoData.visibility = View.INVISIBLE
        }
        notificationAdapter.submitList(data)
    }

    private fun handleError(message: String) {
        binding.tvNoData.visibility = View.VISIBLE
    }
}
