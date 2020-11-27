package com.keelim.nandadiagnosis.ui.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.data.SearchRecyclerItem

class SearchTempFragment : Fragment() {
    private var columnCount = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        if (view is RecyclerView) { // 어댑터 세팅
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = SearchTempRecyclerViewAdapter(SearchRecyclerItem.ITEMS)
            }
        }
        return view
    }

}