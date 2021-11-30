package com.keelim.cnubus.data.api.response


import com.google.gson.annotations.SerializedName

data class ErrorMessage(
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("developerMessage")
    val developerMessage: String? = null,
    @SerializedName("link")
    val link: String? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("total")
    val total: Int? = null
)
