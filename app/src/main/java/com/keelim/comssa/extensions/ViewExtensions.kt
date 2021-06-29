package com.keelim.comssa.extensions

import android.view.View
import androidx.annotation.Px

@Px
fun View.dip(dipValue: Float) = context.dip(dipValue)

fun View.toVisible() {
    visibility = View.VISIBLE
}

fun View.toGone() {
    visibility = View.GONE
}