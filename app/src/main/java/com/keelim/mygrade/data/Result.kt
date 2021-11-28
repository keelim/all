package com.keelim.mygrade.data

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(
    val grade: String,
    val point: String
) : Parcelable
