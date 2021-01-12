package com.keelim.nandadiagnosis.ui.main.search

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)

        return if (view != null) (recyclerView.getChildViewHolder(view) as SearchRecyclerViewAdapter.ViewHolder).getItemDetails()
        else null
    }
}