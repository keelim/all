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
package com.keelim.cnubus.ui.screen.stations

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentStationsBinding
import com.keelim.common.extensions.toGone
import com.keelim.common.extensions.toVisible
import com.keelim.data.model.Station
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StationsFragment : Fragment() {
    private var _binding: FragmentStationsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindViews()
        observeState()
        viewModel.onViewCreated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard()
        _binding = null
    }

    private fun showLoadingIndicator() {
        binding.progressBar.toVisible()
    }

    private fun hideLoadingIndicator() {
        binding.progressBar.toGone()
    }

    private fun showStations(stations: List<Station>) {
        (binding.recyclerView.adapter as? StationsAdapter)?.run {
            submitList(stations)
        }
    }

    private fun initViews() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = StationsAdapter()
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }
    }

    private fun bindViews() {
        binding.searchEditText.addTextChangedListener { editable ->
            viewModel.filterStations(editable.toString())
        }

        (binding.recyclerView.adapter as? StationsAdapter)?.apply {
            onItemClickListener = { station ->
                findNavController().navigate(
                    R.id.stationArrivalsFragment,
                    bundleOf(
                        "station" to station,
                    ),
                )
            }
            onFavoriteClickListener = { station ->
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    private fun observeState() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.state
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collect { state ->
                when (state) {
                    is StationState.HideLoading -> hideLoadingIndicator()
                    is StationState.ShowLoading -> showLoadingIndicator()
                    is StationState.ShowStation -> showStations(state.data)
                    is StationState.UnInitialized -> Unit
                }
            }
    }
}
