package com.keelim.data.repository.linkinspector

import com.keelim.model.linkinspector.HttpResult
import com.keelim.model.linkinspector.OgResult
import com.keelim.model.linkinspector.ResolvedApp

interface LinkInspectorRepository {
    suspend fun resolveApps(url: String): List<ResolvedApp>
    suspend fun checkHttp(url: String): HttpResult?
    suspend fun fetchOg(url: String): OgResult?
}
