package com.keelim.cnubus.ui.subway.stations

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keelim.cnubus.data.model.Station
import com.keelim.cnubus.databinding.FragmentStationsBinding
import com.keelim.cnubus.utils.toGone
import com.keelim.cnubus.utils.toVisible
import com.keelim.common.repeatCallDefaultOnStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class StationsFragment : Fragment() {
    private var _binding: FragmentStationsBinding? = null
    private val viewModel:StationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentStationsBinding.inflate(inflater, container, false)
        .also { _binding = it }
        .root

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

    fun showLoadingIndicator() {
        _binding?.progressBar?.toVisible()
    }

    fun hideLoadingIndicator() {
        _binding?.progressBar?.toGone()
    }

    fun showStations(stations: List<Station>) {
        (_binding?.recyclerView?.adapter as? StationsAdapter)?.run {
            this.data = stations
            notifyDataSetChanged()
        }
    }

    private fun initViews() {
        _binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = StationsAdapter()
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }
    }

    private fun bindViews() {
        _binding?.searchEditText?.addTextChangedListener { editable ->
            viewModel.filterStations(editable.toString())
        }

        (_binding?.recyclerView?.adapter as? StationsAdapter)?.apply {
            onItemClickListener = { station ->
                val action = StationsFragmentDirections.actionStationsDestToStationArrivalsDest(station)
                findNavController().navigate(action)
            }
            onFavoriteClickListener = { station ->
                viewModel.toggleStationFavorite(station)
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    private fun observeState() = viewLifecycleOwner.repeatCallDefaultOnStarted {
        viewModel.state.collect {
            when(it){
                is StationState.HideLoading -> hideLoadingIndicator()
                is StationState.ShowLoading -> showLoadingIndicator()
                is StationState.ShowStation -> showStations(it.data)
                is StationState.UnInitialized -> Unit
            }
        }
    }
}
