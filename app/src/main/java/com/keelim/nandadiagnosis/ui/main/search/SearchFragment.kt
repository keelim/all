package com.keelim.nandadiagnosis.ui.main.search


import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.data.db.AppDatabase
import com.keelim.nandadiagnosis.data.db.NandaEntity
import com.keelim.nandadiagnosis.databinding.FragmentSearchBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


class SearchFragment : Fragment(R.layout.fragment_search) { //frag
    private var fragmentSearchBinding: FragmentSearchBinding? = null
    private var saveList: List<NandaEntity>? = null
    private var trackers: SelectionTracker<Long>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val binding = FragmentSearchBinding.bind(view)
        fragmentSearchBinding = binding
        binding.recyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(RecyclerViewDecoration(0, 10))
        }

//        initTracker()
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
                //검색을 할 수 있게 하는 것
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        val items = searchDiagnosis(query) //검색을 한다.
                        saveList = items
                        fragmentSearchBinding!!.recyclerView.adapter =
                            SearchRecyclerViewAdapter(items).apply {
                                listener =
                                    object : SearchRecyclerViewAdapter.OnSearchItemClickListener {
                                        override fun onSearchItemClick(position: Int) {}
                                        override fun onSearchItemLongClick(position: Int) {}
                                    }
                                notifyDataSetChanged()
                                tracker = trackers
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
            debounce(3000L, TimeUnit.MILLISECONDS)
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

    private fun searchDiagnosis(keyword: String): List<NandaEntity> { //데이터베이스 가져와서 검색하기
        return AppDatabase.getInstance(requireActivity())!!.dataDao().search(keyword)
    }

    private fun initTracker() {
        val recyclerView: RecyclerView = fragmentSearchBinding!!.recyclerView
        trackers = SelectionTracker.Builder<Long>(
            "mySelection",
            recyclerView,
            StableIdKeyProvider(recyclerView),
            MyItemDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        )
            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()

        /*tracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    val nItems: Int? = tracker?.selection!!.size()
                    if (nItems == 2) {
                        launchSum(tracker?.selection!!)
                    }
                }
            })*/
    }

    /*private fun launchSum(selection: Selection<Long>) {
        val list = selection.map {
            adapter.list[it.toInt()]
        }.toList()
        SumActivity.launch(this, list as ArrayList<Int>)
    }*/
}