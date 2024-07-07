package com.keelim.core.network.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NoticeResponse(
    @SerialName("created_at")
    val createdAt: Instant, // 2023-12-04T00:00:00+00:00
    @SerialName("description")
    val description: String = "", // 앱이 사용하고 있는 버전을 업데이트 하였습니다.
    @SerialName("id")
    val id: Int = 0, // 1
    @SerialName("title")
    val title: String = "", // 버전 업데이트
    @SerialName("fixed")
    val fixed: Boolean = false,
)
