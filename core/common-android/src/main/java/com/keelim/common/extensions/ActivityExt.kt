package com.keelim.common.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    val imm: InputMethodManager = getSystemService(InputMethodManager::class.java)
    val view = currentFocus ?: View(this)
    imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Context.getScreenWidth(): Int {
    val displayMetrics = resources.displayMetrics
    return displayMetrics.widthPixels
}
