package com.keelim.comssa.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.toast(message:String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toast(@StringRes message:Int){
    Toast.makeText(this, resources.getText(message), Toast.LENGTH_SHORT).show()
}