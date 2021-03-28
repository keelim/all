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
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.selection.Selection
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.data.db.AppDatabase
import com.keelim.nandadiagnosis.data.db.NandaEntity
import com.keelim.nandadiagnosis.databinding.FragmentSearchBinding
import com.keelim.nandadiagnosis.ui.main.search.selection.MyItemDetailsLookup
import com.keelim.nandadiagnosis.ui.main.search.selection.MyItemKeyProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment(R.layout.fragment_search) { // frag
  private var fragmentSearchBinding: FragmentSearchBinding? = null
  private var trackers: SelectionTracker<Long>? = null
  val viewModel: TempViewModel by viewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setHasOptionsMenu(true)

    val binding = FragmentSearchBinding.bind(view)
    fragmentSearchBinding = binding

//        binding.viewModel = SearchViewModel()

    binding.recyclerView.apply {
      setHasFixedSize(true)
      addItemDecoration(RecyclerViewDecoration(0, 10))
      adapter = SearchRecyclerViewAdapter().apply {
        setNandaItem(listOf())
        listener = object : SearchRecyclerViewAdapter.OnSearchItemClickListener {
          override fun onSearchItemClick(position: Int) {
          }
          override fun onSearchItemLongClick(position: Int) {
          }
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

        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
          override fun onQueryTextSubmit(query: String): Boolean {
            val items = searchDiagnosis(query) // 검색을 한다.
            (fragmentSearchBinding!!.recyclerView.adapter as SearchRecyclerViewAdapter).apply {
              setNandaItem(items)
              notifyDataSetChanged()
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

  override fun onDestroyView() {
    fragmentSearchBinding = null
    super.onDestroyView()
  }

  private fun searchDiagnosis(keyword: String): List<NandaEntity> { // 데이터베이스 가져와서 검색하기
    return AppDatabase.getInstance(requireActivity())!!.dataDao().search(keyword)
  }

  private fun initTracker() {
    trackers = SelectionTracker.Builder(
      "mySelection",
      fragmentSearchBinding!!.recyclerView,
      MyItemKeyProvider(fragmentSearchBinding!!.recyclerView),
      MyItemDetailsLookup(fragmentSearchBinding!!.recyclerView),
      StorageStrategy.createLongStorage()
    ).withSelectionPredicate(SelectionPredicates.createSelectAnything())
      .build()
    (fragmentSearchBinding!!.recyclerView.adapter as (SearchRecyclerViewAdapter)).apply {
      tracker = trackers
    }
  }

  private fun multiSelection(selection: Selection<Long>) {
    var s = ""
    val list = selection.map { (fragmentSearchBinding!!.recyclerView.adapter as SearchRecyclerViewAdapter).getItem(it.toInt()) }
    list.forEach { s += it.toString() + "\n" }

    shareInformation(s)
  }

  private fun shareInformation(s: String) {
    val chooser = Intent.createChooser(
      Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, s)
      },
      "내용 공유하기"
    )
    requireActivity().startActivity(chooser)
  }
}
