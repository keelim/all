package com.keelim.model.linkinspector

data class ResolvedApp(
    val label: String,
    val packageName: String,
)

data class HttpResult(
    val statusCode: Int,
    val finalUrl: String,
    val headers: Map<String, List<String>> = emptyMap(),
    val redirects: List<String> = emptyList(),
)

data class OgResult(
    val title: String?,
    val description: String?,
    val image: String?,
)
