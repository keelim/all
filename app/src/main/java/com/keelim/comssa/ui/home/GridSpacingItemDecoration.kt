package com.keelim.comssa.ui.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val adapterPosition = parent.getChildAdapterPosition(view)
        val gridLayoutManager = parent.layoutManager as GridLayoutManager
        val spanSize = gridLayoutManager.spanSizeLookup.getSpanSize(adapterPosition)

        if (spanSize == spanCount) {
            outRect.left = spacing
            outRect.right = spacing
            outRect.top = spacing
            outRect.bottom = spacing
            return
        }

        val column = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
        val itemHorizontalSpacing = ((spanCount + 1) * spacing) / spanCount.toFloat()
        when (column) {
            0 -> {
                outRect.left = spacing
                outRect.right = (itemHorizontalSpacing - spacing).toInt()
            }
            (spanCount - 1) -> {
                outRect.left = (itemHorizontalSpacing - spacing).toInt()
                outRect.right = spacing
            }
            else -> {
                outRect.left = (itemHorizontalSpacing / 2).toInt()
                outRect.right = (itemHorizontalSpacing / 2).toInt()
            }
        }
        outRect.top = spacing
        outRect.bottom = spacing
    }
}