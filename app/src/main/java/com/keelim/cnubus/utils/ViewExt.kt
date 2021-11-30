package com.keelim.cnubus.utils

import android.view.View
import androidx.annotation.Px
import com.keelim.cnubus.utils.dip

@Px
fun View.dip(dipValue: Float) = context.dip(dipValue)

fun View.toVisible() {
    visibility = View.VISIBLE
}

fun View.toGone() {
    visibility = View.GONE
}
