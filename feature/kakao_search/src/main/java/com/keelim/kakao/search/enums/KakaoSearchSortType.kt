package com.keelim.kakao.search.enums

import com.google.gson.annotations.SerializedName

enum class KakaoSearchSortType {
    @SerializedName("accuracy")
    ACCURACY,

    @SerializedName("recency")
    RECENCY,
}
