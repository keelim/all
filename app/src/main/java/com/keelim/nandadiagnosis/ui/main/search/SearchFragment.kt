/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
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
package com.keelim.nandadiagnosis.ui.main.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.keelim.common.toast
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.databinding.FragmentSearchBinding
import com.keelim.nandadiagnosis.ui.main.search.history.HistoryAdapter
import com.keelim.nandadiagnosis.ui.main.search.selection.MyItemDetailsLookup
import com.keelim.nandadiagnosis.ui.main.search.selection.MyItemKeyProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment() {
  private var trackers: SelectionTracker<Long>? = null

  private var _binding: FragmentSearchBinding? = null
  private val binding get() = _binding!!

  private val viewModel: SearchViewModel by viewModels()

  private val historyAdapter = HistoryAdapter(
    historyDeleteListener = {
      deleteSearch(it)
    },
    textSelectListener = {},
  )
  private val searchRecyclerViewAdapter3 = SearchRecyclerViewAdapter3(
    favoriteListener = { favorite, id ->
      favoriteUpdate(favorite, id)
    }
  )

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setHasOptionsMenu(true)
    initViews()
    observeFlow()
  }

  private fun observeFlow() = viewLifecycleOwner.lifecycleScope.launch {
    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
      viewModel.state.collect {
        when (it) {
          is SearchListState.UnInitialized -> requireActivity().toast("데이터 설정 중입니다.")
          is SearchListState.Loading -> requireContext().toast("데이터 로딩 중")
          is SearchListState.Searching -> {
            handleSuccess(it)
          }
          is SearchListState.Error -> Unit
          is SearchListState.Success -> Unit
        }
      }
      viewModel.history.collect {
        historyAdapter.submitList(it)
        binding.historyRecycler.isVisible = true
      }
    }
  }

  private fun initViews() = with(binding) {
    historyRecycler.adapter = historyAdapter
    recyclerView.apply {
      setHasFixedSize(true)
      addItemDecoration(RecyclerViewDecoration(0, 10))
      adapter = searchRecyclerViewAdapter3
    }
    initTracker()
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.search_menu, menu)
    val item = menu.findItem(R.id.menu_search)

    val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
    val searchView = item.actionView as SearchView
    showHistoryView()

    searchView.apply {
      setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
      isSubmitButtonEnabled = true
      isQueryRefinementEnabled = true

      setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
          saveSearchKeyword(query)
          Timber.d("데이터베이스 $query")
          viewModel.search2(query.replace("\\s", ""))
          search(query.replace("\\s", ""))
          return true
        }
        override fun onQueryTextChange(newText: String): Boolean = true
      })
    }

    super.onCreateOptionsMenu(menu, inflater)
  }

  private fun initTracker() {
    trackers = SelectionTracker.Builder(
      "mySelection",
      binding.recyclerView,
      MyItemKeyProvider(binding.recyclerView),
      MyItemDetailsLookup(binding.recyclerView),
      StorageStrategy.createLongStorage()
    )
      .withSelectionPredicate(SelectionPredicates.createSelectAnything())
      .build()

    searchRecyclerViewAdapter3.tracker = trackers
    binding.floating.setOnClickListener {
//      multiSelection(trackers?.selection!!)
    }
  }

  private fun shareInformation(s: String) {
    requireActivity().startActivity(
      Intent.createChooser(
        Intent(Intent.ACTION_SEND).apply {
          type = "text/plain"
          putExtra(Intent.EXTRA_TEXT, s)
        },
        "내용 공유하기"
      )
    )
  }

  private fun saveSearchKeyword(keyword: String) {
    viewModel.saveHistory(keyword)
  }

  private fun deleteSearch(keyword: String) {
    viewModel.deleteHistory(keyword)
    showHistoryView()
  }

  private fun favoriteUpdate(favorite: Int, id: Int) {
    viewModel.favoriteUpdate(favorite, id)
  }

  private fun showHistoryView() = lifecycleScope.launch {
    viewModel.getAllHistories()
  }

  private fun hideHistoryView() {
    binding.historyRecycler.isVisible = false
  }

  private fun handleSuccess(state: SearchListState.Searching) {
    hideHistoryView()
    if (state.searchList.isEmpty()) {
      Timber.d("결과 값이 비어 있습니다.")
    } else {
//      searchRecyclerViewAdapter2.submitList(state.searchList)
    }
  }

  private fun search(query: String) = viewLifecycleOwner.lifecycleScope.launch {
    viewModel.getContent(query).collectLatest {
      searchRecyclerViewAdapter3.submitData(it)
    }
  }
}
