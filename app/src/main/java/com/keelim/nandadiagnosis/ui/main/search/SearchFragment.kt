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
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.selection.Selection
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.keelim.common.toast
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.data.db.AppDatabaseV2
import com.keelim.nandadiagnosis.data.db.entity.History
import com.keelim.nandadiagnosis.databinding.FragmentSearchBinding
import com.keelim.nandadiagnosis.ui.main.search.history.HistoryAdapter
import com.keelim.nandadiagnosis.ui.main.search.selection.MyItemDetailsLookup
import com.keelim.nandadiagnosis.ui.main.search.selection.MyItemKeyProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment() {
  private var trackers: SelectionTracker<Long>? = null
  private var _binding: FragmentSearchBinding? = null
  private val binding get() = _binding!!
  private val scope = MainScope()
  private lateinit var db: AppDatabaseV2
  private lateinit var historyAdapter: HistoryAdapter
  private lateinit var searchRecyclerViewAdapter2: SearchRecyclerViewAdapter2
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
    scope.cancel()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setHasOptionsMenu(true)

    historyAdapter = HistoryAdapter(
      historyDeleteListener = {
        deleteSearch(it)
      },
      textSelectListener = {},
    )
    binding.historyRecycler.adapter = historyAdapter
    searchRecyclerViewAdapter2 = SearchRecyclerViewAdapter2(
      favoriteListener = { favorite, id ->
        favoriteUpdate(favorite, id)
      }
    ).apply {
      submitList(listOf())
    }
    binding.recyclerView.apply {
      setHasFixedSize(true)
      addItemDecoration(RecyclerViewDecoration(0, 10))
      adapter = searchRecyclerViewAdapter2
    }

    initTracker()
    db = AppDatabaseV2.getInstance(requireContext())!!
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

          scope.launch {
            val items = db.dataDao.search(query.replace("\\s", ""))
            CoroutineScope(Dispatchers.Main).launch {
              hideHistoryView()
              if (items.isNotEmpty()) {
                searchRecyclerViewAdapter2.submitList(items)
                searchRecyclerViewAdapter2.notifyDataSetChanged()
                Timber.d("Save the query")
              } else {
                toast("검색되는 항목이 없습니다.")
              }
            }
          }
          return true
        }
        override fun onQueryTextChange(newText: String): Boolean {
          return true
        }
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

    (binding.recyclerView.adapter as (SearchRecyclerViewAdapter2)).apply {
      tracker = trackers
    }

    binding.floating.setOnClickListener {
      multiSelection(trackers?.selection!!)
    }
  }

  private fun multiSelection(selection: Selection<Long>) {
    val list2 =
      selection.map { (binding.recyclerView.adapter as SearchRecyclerViewAdapter2).getItem(it.toInt()) }
    if (list2.isEmpty()) {
      Toast.makeText(requireActivity(), "데이터를 선택해주세요", Toast.LENGTH_SHORT).show()
    } else {
      shareInformation(list2.fold("") { acc, entity -> acc + entity.toString() })
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
    scope.launch {
      db.historyDao.insertHistory(History(null, keyword))
    }
  }

  private fun deleteSearch(keyword: String) {
    scope.launch {
      db.historyDao.delete(keyword)
      showHistoryView()
    }
  }

  private fun favoriteUpdate(favorite: Int, id: Int) {
    CoroutineScope(Dispatchers.IO).launch {
      when (favorite) {
        1 -> db.dataDao.favoriteUpdate(0, id)
        0 -> db.dataDao.favoriteUpdate(1, id)
        else -> Unit
      }
    }
  }

  private fun showHistoryView() {
    scope.launch {
      val keywords = withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
        db.historyDao.getAll().reversed()
      }
      Timber.d("데이터베이스 $keywords")
      CoroutineScope(Dispatchers.Main).launch {
        binding.historyRecycler.isVisible = true
        historyAdapter.submitList(keywords)
      }
    }
    binding.historyRecycler.isVisible = true
  }

  private fun hideHistoryView() {
    binding.historyRecycler.isVisible = false
  }
}
