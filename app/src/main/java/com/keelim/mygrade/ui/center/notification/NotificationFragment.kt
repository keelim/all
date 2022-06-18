package com.keelim.mygrade.ui.center.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearSnapHelper
import com.keelim.common.extensions.repeatCallDefaultOnStarted
import com.keelim.common.extensions.toGone
import com.keelim.common.extensions.toVisible
import com.keelim.common.extensions.toast
import com.keelim.data.model.notification.Notification
import com.keelim.mygrade.databinding.FragmentNotificationBinding
import dagger.hilt.android.AndroidEntryPoint
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

    private fun observeState() = viewLifecycleOwner.lifecycleScope.launch {
        repeatCallDefaultOnStarted {
            viewModel.state.collect {
                when (it) {
                    is NotificationState.UnInitialized -> {
                        binding.loading.toVisible()
                    }
                    is NotificationState.Loading -> {
                        binding.loading.toVisible()
                    }
                    is NotificationState.Success ->{
                        handleSuccess(it.data)
                    }
                    is NotificationState.Error -> {
                        binding.loading.toGone()
                        binding.tvNoData.toVisible()
                        toast(it.message)
                    }
                }
            }
        }
    }

    private fun initViews() = with(binding) {

        notificationRecycler.apply {
            val snapHelper = LinearSnapHelper()
            adapter = notificationAdapter.apply {
                doOnNextLayout {
                }
            }
            snapHelper.attachToRecyclerView(this)
        }
    }

    private fun handleSuccess(data: List<Notification>) {
        binding.loading.toGone()
        if (data.isEmpty()) {
            binding.tvNoData.toVisible()
        } else {
            binding.tvNoData.toGone()
        }
        notificationAdapter.submitList(data)
    }
}
