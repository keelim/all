package com.keelim.mygrade.ui.center.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import com.keelim.common.extensions.toGone
import com.keelim.common.extensions.toVisible
import com.keelim.data.model.notification.Notification
import com.keelim.data.repository.IoRepository
import com.keelim.mygrade.databinding.FragmentNotificationBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val ioRepository: IoRepository,
) : ViewModel()
@AndroidEntryPoint
class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val viewModel by viewModels<NotificationViewModel>()
    private val notificationAdapter by lazy { NotificationAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentNotificationBinding.inflate(inflater, container, false)
        .apply {
            notificationRecycler.apply {
                adapter = notificationAdapter.apply {
                    doOnNextLayout {}
                }
                LinearSnapHelper().attachToRecyclerView(this)
            }
            imageviewBack.setOnClickListener {
                findNavController().navigateUp()
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

sealed class NotificationState {
    object UnInitialized : NotificationState()
    object Loading : NotificationState()
    data class Error(
        val message: String,
    ) : NotificationState()

    data class Success(
        val data: List<Notification>,
    ) : NotificationState()
}
