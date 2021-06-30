package com.keelim.comssa.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keelim.comssa.databinding.FragmentHomeBinding
import com.keelim.comssa.extensions.dip
import com.keelim.comssa.extensions.toGone
import com.keelim.comssa.extensions.toVisible
import com.keelim.comssa.ui.home.HomeAdapter.Companion.ITEM_VIEW_TYPE_FEATURED
import com.keelim.comssa.ui.home.HomeAdapter.Companion.ITEM_VIEW_TYPE_SECTION_HEADER
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: Fragment() {
    private val viewModel:HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeAdapter = HomeAdapter(
        onDataClickListener = {

        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        fetchRandom()
        fetchData()
        viewModel.fetchData()
    }

    private fun showLoadingIndicator() {
        binding.progressBar.toVisible()
    }

    private fun hideLoadingIndicator() {
        binding.progressBar.toGone()
    }

    private fun showErrorDescription(message: String) = with(binding) {
        recyclerView.toGone()
        errorDescriptionTextView.toVisible()
        errorDescriptionTextView.text = message
    }

    private fun showDatas() = with(binding) {
        recyclerView.toVisible()
        errorDescriptionTextView.toGone()
        homeAdapter.apply {
            addData(viewModel.randomData.value, viewModel.allData.value.orEmpty())
            notifyDataSetChanged()
        }
    }

    private fun initViews() = with(binding) {
        recyclerView.apply {
            adapter = homeAdapter
            val gridLayoutManager = createGridLayoutManager()
            layoutManager = gridLayoutManager
            addItemDecoration(GridSpacingItemDecoration(gridLayoutManager.spanCount, dip(6f)))
        }
    }

    private fun RecyclerView.createGridLayoutManager(): GridLayoutManager = GridLayoutManager(
        context,
        3,
        RecyclerView.VERTICAL,
        false
    ).apply {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter?.getItemViewType(position)) {
                    ITEM_VIEW_TYPE_SECTION_HEADER, ITEM_VIEW_TYPE_FEATURED -> spanCount
                    else -> 1
                }
            }
        }
    }

    private fun recyclerUpdate() {
        try {
            showLoadingIndicator()
            showDatas()
        } catch (exception: Exception) {
            exception.printStackTrace()
            showErrorDescription("에러가 발생했어요 😢")
        } finally {
            hideLoadingIndicator()
        }
    }

    private fun fetchData() = viewModel.allData.observe(viewLifecycleOwner, {
        recyclerUpdate()
    })

    private fun fetchRandom() = viewModel.randomData.observe(viewLifecycleOwner, {
        recyclerUpdate()
    })
}