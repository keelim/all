package com.keelim.reference_search

import com.google.gson.annotations.SerializedName

data class Reference(
    @SerializedName("itemId") val lastBuildDate: String,
    @SerializedName("total") val total: Int,
    @SerializedName("start") val start: Int,
    @SerializedName("display") val display: Int,
    @SerializedName("title") val title: String,
    @SerializedName("link") val link: String,
    @SerializedName("image") val image: String,
    @SerializedName("author") val author: String,
    @SerializedName("price") val price: Int,
    @SerializedName("discount") val discount: Int,
    @SerializedName("publisher") val publisher: String,
    @SerializedName("isbn") val isbn: Int,
    @SerializedName("description") val description: String,
    @SerializedName("pubdate") val pubdate: String,
)
