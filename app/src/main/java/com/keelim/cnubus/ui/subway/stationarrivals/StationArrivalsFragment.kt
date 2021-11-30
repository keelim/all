package com.keelim.cnubus.ui.subway.stationarrivals

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keelim.cnubus.R
import com.keelim.cnubus.data.model.ArrivalInformation
import com.keelim.cnubus.databinding.FragmentStationArrivalsBinding
import com.keelim.cnubus.ui.subway.stations.StationState
import com.keelim.cnubus.utils.toGone
import com.keelim.cnubus.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StationArrivalsFragment : Fragment() {

    private var _binding: FragmentStationArrivalsBinding? = null
    private val binding get() = _binding!!
    private val arguments: StationArrivalsFragmentArgs by navArgs()
    private val viewModel:StationArrivalsViewModel by viewModels()

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

    private fun observeState() = viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED){
            viewModel.state.collect {
                when(it){
                    is ArrivalState.HideLoading -> hideLoadingIndicator()
                    is ArrivalState.ShowLoading -> showLoadingIndicator()
                    is ArrivalState. ShowStationArrivals-> showStationArrivals(it.data)
                    is ArrivalState.UnInitialized -> Unit
                    is ArrivalState.Error -> {
                        showErrorDescription(it.message)
                    }
                }
            }
        }
    }
}
