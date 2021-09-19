package com.keelim.cnubus.data.model

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class Developer(
    val name: String,
    val photoUrl: String,
    val companyName: String? = null,
    val snsLink: String? = null
):Parcelable