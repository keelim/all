package com.keelim.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GradeResult(
    val grade: String,
    val point: String,
) : Parcelable
