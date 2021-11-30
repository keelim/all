package com.keelim.cnubus.data.api.response


import com.google.gson.annotations.SerializedName
import java.util.*

data class RealtimeArrival(
    @SerializedName("arvlCd")
    val arvlCd: String? = null,
    @SerializedName("arvlMsg2")
    val arvlMsg2: String? = null,
    @SerializedName("arvlMsg3")
    val arvlMsg3: String? = null,
    @SerializedName("barvlDt")
    val barvlDt: String? = null,
    @SerializedName("beginRow")
    val beginRow: Any? = null,
    @SerializedName("bstatnId")
    val bstatnId: String? = null,
    @SerializedName("bstatnNm")
    val bstatnNm: String? = null,
    @SerializedName("btrainNo")
    val btrainNo: String? = null,
    @SerializedName("btrainSttus")
    val btrainSttus: String? = null,
    @SerializedName("curPage")
    val curPage: Any? = null,
    @SerializedName("endRow")
    val endRow: Any? = null,
    @SerializedName("ordkey")
    val ordkey: String? = null,
    @SerializedName("pageRow")
    val pageRow: Any? = null,
    @SerializedName("recptnDt")
    val recptnDt: String? = null,
    @SerializedName("rowNum")
    val rowNum: Int? = null,
    @SerializedName("selectedCount")
    val selectedCount: Int? = null,
    @SerializedName("statnFid")
    val statnFid: String? = null,
    @SerializedName("statnId")
    val statnId: String? = null,
    @SerializedName("statnList")
    val statnList: String? = null,
    @SerializedName("statnNm")
    val statnNm: String? = null,
    @SerializedName("statnTid")
    val statnTid: String? = null,
    @SerializedName("subwayHeading")
    val subwayHeading: String? = null,
    @SerializedName("subwayId")
    val subwayId: Int = 0,
    @SerializedName("subwayList")
    val subwayList: String? = null,
    @SerializedName("subwayNm")
    val subwayNm: Any? = null,
    @SerializedName("totalCount")
    val totalCount: Int? = null,
    @SerializedName("trainCo")
    val trainCo: Any? = null,
    @SerializedName("trainLineNm")
    val trainLineNm: String? = null,
    @SerializedName("updnLine")
    val updnLine: String? = null
)
