package com.keelim.nandadiagnosis.ui.main.search

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewDecoration(private val divWidth:Int, private val divHeight: Int) : RecyclerView.ItemDecoration() {
    private var height:Int = divHeight
    private var width: Int = divWidth

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = height
        outRect.right = width
    }
}