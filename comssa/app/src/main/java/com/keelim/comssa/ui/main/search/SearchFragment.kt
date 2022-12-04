package com.keelim.comssa.ui.main.search

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.keelim.comssa.R
import com.keelim.comssa.databinding.FragmentSearchBinding
import com.keelim.comssa.extensions.toast
import com.keelim.comssa.provides.SuggestionProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private val itemAdapter = SearchAdapter { favorite, id ->
        viewModel.favorite(favorite, id)
        requireContext().toast("관심 목록에 등록하였습니다.")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        initViews()
    }

    private fun initViews() = with(binding) {
        searchSection.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean = true
                override fun onQueryTextChange(query: String): Boolean {
                    search2(query.replace("\\s", ""))
                    return true
                }
            })
        }

        if (Intent.ACTION_SEARCH == requireActivity().intent.action) {
            requireActivity().intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                SearchRecentSuggestions(
                    requireContext(),
                    SuggestionProvider.AUTHORITY,
                    SuggestionProvider.MODE
                ).saveRecentQuery(query, null)
            }
        }

        with(recycler) {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(requireContext())
            val snap = LinearSnapHelper()
            snap.attachToRecyclerView(recycler)
        }

        bottomButton.setOnClickListener {
            requireContext().toast("기능 준비중 입니다. 조금만 기다려주세요.")
        }
    }

    private fun search2(query: String) = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                viewModel.getContent(query).collectLatest {
                    itemAdapter.submitData(it)
                }
            }
        }
    }
}
