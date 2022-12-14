/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.cnubus.ui.root.basic

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentRootBinding
import com.keelim.common.extensions.toast
import com.keelim.data.model.gps.Location
import com.keelim.map.MapEvent
import com.keelim.map.MapsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RootFragment : Fragment(R.layout.fragment_root) {
    private lateinit var binding: FragmentRootBinding
    private val viewModel: RootViewModel by viewModels()

    private val mode by lazy { requireArguments().getString("mode") }

    private val rootAdapter by lazy {
        RootAdapter(
            click = { position ->
                viewModel.insertHistory(position, mode)
                val data = when (mode) {
                    "a" -> viewModel.data.value[position].roota
                    "b" -> viewModel.data.value[position].rootb
                    "c" -> viewModel.data.value[position].rootc
                    else -> null
                }
                data?.let { value ->
                    startActivity(
                        Intent(requireContext(), MapsActivity::class.java).apply {
                            putExtra("location", value - 1)
                            putExtra("mode", mode)
                        }
                    )
                } ?: requireActivity().toast("?????? ?????? ????????????. ")
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRootBinding.bind(view)
        modeSetting()
        initViews()
        observeState()
    }

    private fun modeSetting() {
        viewModel.rootChange(mode ?: "")
    }

    private fun initViews() = with(binding) {
        lvAroot.run {
            setHasFixedSize(true)
            adapter = rootAdapter
        }
    }

    private fun observeState() = lifecycleScope.launch {
        viewModel.state
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collect { event ->
                when (event) {
                    is MapEvent.UnInitialized -> Unit
                    is MapEvent.Loading -> Unit
                    is MapEvent.MigrateSuccess -> {
                        if (event.data.isEmpty()) {
                            rootAdapter.submitList(
                                listOf(
                                    Location.defaultLocation().apply {
                                        name = "?????? ???????????? ????????? ????????????."
                                    }
                                )
                            )
                        } else {
                            rootAdapter.submitList(event.data)
                        }
                    }
                    is MapEvent.Error -> requireContext().toast(event.message)
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
