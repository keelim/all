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
package com.keelim.cnubus.ui.subway.stationarrivals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keelim.cnubus.R
import com.keelim.cnubus.data.model.ArrivalInformation
import com.keelim.cnubus.databinding.FragmentStationArrivalsBinding
import com.keelim.cnubus.utils.toGone
import com.keelim.cnubus.utils.toVisible
import com.keelim.common.repeatCallDefaultOnStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class StationArrivalsFragment : Fragment() {

    private var _binding: FragmentStationArrivalsBinding? = null
    private val binding get() = _binding!!
    private val arguments: StationArrivalsFragmentArgs by navArgs()
    private val viewModel: StationArrivalsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentStationArrivalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchStationArrivals()
        viewModel.setStation(arguments.station)
        initViews()
        observeState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_station_arrivals, menu)
        menu.findItem(R.id.favoriteAction).apply {
            setIcon(
                if (arguments.station.isFavorited) {
                    R.drawable.ic_star
                } else {
                    R.drawable.ic_star_empty
                }
            )
            isChecked = arguments.station.isFavorited
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.refreshAction -> {
                viewModel.fetchStationArrivals()
                true
            }
            R.id.favoriteAction -> {
                item.isChecked = !item.isChecked
                item.setIcon(
                    if (item.isChecked) {
                        R.drawable.ic_star
                    } else {
                        R.drawable.ic_star_empty
                    }
                )
                viewModel.toggleStationFavorite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun showLoadingIndicator() {
        _binding?.progressBar?.visibility = View.VISIBLE
    }

    private fun hideLoadingIndicator() {
        _binding?.progressBar?.visibility = View.GONE
    }

    private fun showErrorDescription(message: String) {
        _binding?.recyclerView?.toGone()
        _binding?.errorDescriptionTextView?.toVisible()
        _binding?.errorDescriptionTextView?.text = message
    }

    private fun showStationArrivals(arrivalInformation: List<ArrivalInformation>) {
        _binding?.errorDescriptionTextView?.toGone()
        (_binding?.recyclerView?.adapter as? StationArrivalsAdapter)?.run {
            this.data = arrivalInformation
            notifyDataSetChanged()
        }
    }

    private fun initViews() {
        _binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = StationArrivalsAdapter()
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }
    }

    private fun observeState() = viewLifecycleOwner.repeatCallDefaultOnStarted {
        viewModel.state.collect {
            when (it) {
                is ArrivalState.HideLoading -> hideLoadingIndicator()
                is ArrivalState.ShowLoading -> showLoadingIndicator()
                is ArrivalState.ShowStationArrivals -> showStationArrivals(it.data)
                is ArrivalState.UnInitialized -> Unit
                is ArrivalState.Error -> {
                    showErrorDescription(it.message)
                }
            }
        }
    }
}
