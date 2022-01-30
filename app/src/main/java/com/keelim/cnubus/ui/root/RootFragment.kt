package com.keelim.cnubus.ui.root

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.keelim.cnubus.databinding.FragmentRootBinding
import com.keelim.cnubus.feature.map.ui.MapEvent
import com.keelim.cnubus.feature.map.ui.MapsActivity
import com.keelim.common.extensions.repeatCallDefaultOnStarted
import com.keelim.common.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RootFragment : Fragment() {
    private var _binding: FragmentRootBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RootViewModel by viewModels()
    private val mode by lazy {
        requireArguments().getString("mode")
    }
    private val rootAdapter by lazy {
        RootAdapter(
            click = { position ->
                when (mode) {
                    "a" -> startActivity(
                        Intent(requireContext(), MapsActivity::class.java).apply {
                            putExtra("location", viewModel.data.value[position].roota)
                        }
                    )
                    "b" -> startActivity(
                        Intent(requireContext(), MapsActivity::class.java).apply {
                            putExtra("location", viewModel.data.value[position].rootb)
                        }
                    )
                    "c" -> startActivity(
                        Intent(requireContext(), MapsActivity::class.java).apply {
                            putExtra("location", viewModel.data.value[position].rootc)
                        }
                    )
                    else -> {
                        requireActivity().toast("노선 준비 중입니다. ")
                    }
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRootBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modeSetting()
        initViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun modeSetting() {
        viewModel.rootChange(mode ?: "")
    }

    private fun initViews() = with(binding) {
        lvAroot.setHasFixedSize(true)
        lvAroot.adapter = rootAdapter
        viewLifecycleOwner.repeatCallDefaultOnStarted {
            viewModel.state.collect {
                when (it) {
                    is MapEvent.UnInitialized -> Unit
                    is MapEvent.Loading -> Unit
                    is MapEvent.MigrateSuccess -> rootAdapter.submitList(it.data)
                    is MapEvent.Error -> requireContext().toast(it.message)
                }
            }
        }
    }

    companion object {
        fun newInstance(mode: String): RootFragment {
            return RootFragment().apply {
                arguments = bundleOf(
                    "mode" to mode
                )
            }
        }
    }
}