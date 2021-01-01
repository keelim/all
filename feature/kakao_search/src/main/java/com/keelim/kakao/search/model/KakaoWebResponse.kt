package com.keelim.kakao.search.model


import com.google.gson.annotations.SerializedName

data class KakaoWebResponse(
    @SerializedName("contents")
    val contents: String,
    @SerializedName("datetime")
    val datetime: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String,
)
