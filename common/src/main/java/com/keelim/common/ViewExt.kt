package com.keelim.common

import android.content.Context
import android.view.View

fun View.toVisible(){
    visibility = View.VISIBLE
}

fun View.toGone(){
    visibility = View.GONE
}

fun View.toInvisible(){
    visibility = View.INVISIBLE
}
