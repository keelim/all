package com.keelim.common

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.toast(@StringRes message:Int, duration:Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, message, duration).show()
}

fun Fragment.toast(message:String, duration:Int = Toast.LENGTH_SHORT){
    Toast.makeText(requireContext(), message, duration).show()
}

fun Fragment.toast(@StringRes message:Int, duration:Int = Toast.LENGTH_SHORT){
    Toast.makeText(requireContext(), message, duration).show()
}


