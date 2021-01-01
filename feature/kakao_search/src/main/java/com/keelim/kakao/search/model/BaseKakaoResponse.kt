package com.keelim.kakao.search.model

import com.google.gson.annotations.SerializedName

data class BaseKakaoResponse<T>(
    @SerializedName("documents")
    val documents: List<T>,
    @SerializedName("meta")
    val meta: Meta
){
    data class Meta(
        @SerializedName("is_end")
        val isEnd: Boolean,
        @SerializedName("pageable_count")
        val pageableCount: Int,
        @SerializedName("total_count")
        val totalCount: Int
    )
}
