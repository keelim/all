package com.keelim.common

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun View.toVisible(){
    visibility = View.VISIBLE
}

fun View.toGone(){
    visibility = View.GONE
}

fun View.toInvisible(){
    visibility = View.INVISIBLE
}

fun Context.snack(view:View, message:String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}

fun Context.snack(view:View, @StringRes message:Int) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}
