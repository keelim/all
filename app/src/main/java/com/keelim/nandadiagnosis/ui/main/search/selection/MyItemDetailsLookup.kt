package com.keelim.nandadiagnosis.ui.main.search.selection

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.keelim.nandadiagnosis.ui.main.search.SearchRecyclerViewAdapter

class MyItemDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as SearchRecyclerViewAdapter.ViewHolder)
                .getItemDetails()
        }
        return null
    }
}