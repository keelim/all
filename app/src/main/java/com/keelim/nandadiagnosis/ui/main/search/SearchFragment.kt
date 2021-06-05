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
import android.provider.SearchRecentSuggestions
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.selection.Selection
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.data.db.AppDatabaseV2
import com.keelim.nandadiagnosis.data.db.NandaEntity
import com.keelim.nandadiagnosis.databinding.FragmentSearchBinding
import com.keelim.nandadiagnosis.ui.main.search.selection.MyItemDetailsLookup
import com.keelim.nandadiagnosis.ui.main.search.selection.MyItemKeyProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment(R.layout.fragment_search) { // frag
  private var trackers: SelectionTracker<Long>? = null
  private var _binding: FragmentSearchBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
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

    binding.recyclerView.apply {
      setHasFixedSize(true)
      addItemDecoration(RecyclerViewDecoration(0, 10))
      adapter = SearchRecyclerViewAdapter().apply {
        setNandaItem(listOf())
        listener = object : SearchRecyclerViewAdapter.OnSearchItemClickListener {
          override fun onSearchItemClick(position: Int) {}
          override fun onSearchItemLongClick(position: Int) {}
        }
      }
    }
    initTracker()
    binding.floating.setOnClickListener {
      multiSelection(trackers?.selection!!)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.search_menu, menu)
    val item = menu.findItem(R.id.menu_search)

    val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
    val searchView = item.actionView as SearchView

    Observable.create<CharSequence> { emitter ->
      searchView.apply {
        setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        isSubmitButtonEnabled = true
        isQueryRefinementEnabled = true

        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
          override fun onQueryTextSubmit(query: String): Boolean {
            val items = searchDiagnosis(query.replace("\\s", "")) // 검색을 한다.
            if (items.isNotEmpty()) {
              (binding.recyclerView.adapter as SearchRecyclerViewAdapter).apply {
                setNandaItem(items)
                notifyDataSetChanged()
              }

              SearchRecentSuggestions(
                requireActivity(),
                SuggestionProvider.AUTHORITY,
                SuggestionProvider.MODE
              )
                .saveRecentQuery(query, null)
              Timber.d("Save the query")
            } else {
              Toast.makeText(requireActivity(), "검색되는 항목이 없습니다.", Toast.LENGTH_SHORT)
                .show()
            }
            return true
          }

          override fun onQueryTextChange(newText: String): Boolean {
            newText.let { emitter.onNext(it) }
            return true
          }
        })
      }
    }.apply {
      debounce(1000L, TimeUnit.MILLISECONDS)
        .filter { !TextUtils.isEmpty(it) }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          Toast.makeText(requireActivity(), "searching => $it", Toast.LENGTH_SHORT).show()
        }
    }
    super.onCreateOptionsMenu(menu, inflater)
  }
  
  private fun searchDiagnosis(keyword: String): List<NandaEntity> { // 데이터베이스 가져와서 검색하기
    return runBlocking {
      AppDatabaseV2.getInstance(requireActivity())!!.dataDao.search(keyword)
    }
  }

  private fun initTracker() {
    trackers = SelectionTracker.Builder(
      "mySelection",
      binding.recyclerView,
      MyItemKeyProvider(binding.recyclerView),
      MyItemDetailsLookup(binding!!.recyclerView),
      StorageStrategy.createLongStorage()
    ).withSelectionPredicate(SelectionPredicates.createSelectAnything())
      .build()

    (binding.recyclerView.adapter as (SearchRecyclerViewAdapter)).apply {
      tracker = trackers
    }
  }

  private fun multiSelection(selection: Selection<Long>) {
    var s = ""
    val list =
      selection.map { (binding.recyclerView.adapter as SearchRecyclerViewAdapter).getItem(it.toInt()) }
    if (list.isEmpty()) {
      Toast.makeText(requireActivity(), "데이터를 선택해주세요", Toast.LENGTH_SHORT).show()
    } else {
      list.forEach { s += it.toString() + "\n" }
      shareInformation(s)
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
}
