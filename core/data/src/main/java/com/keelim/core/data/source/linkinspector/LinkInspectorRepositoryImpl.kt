package com.keelim.core.data.source.linkinspector

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.keelim.core.network.di.KtorNetworkModule
import com.keelim.data.repository.linkinspector.LinkInspectorRepository
import com.keelim.model.linkinspector.HttpResult
import com.keelim.model.linkinspector.OgResult
import com.keelim.model.linkinspector.ResolvedApp
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject

class LinkInspectorRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @KtorNetworkModule.KtorAndroidClient private val client: HttpClient,
) : LinkInspectorRepository {
    override suspend fun resolveApps(url: String): List<ResolvedApp> = withContext(Dispatchers.Default) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val pm = context.packageManager
        val list = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        list.map {
            ResolvedApp(
                label = it.loadLabel(pm).toString(),
                packageName = it.activityInfo.packageName,
            )
        }.sortedBy { it.label.lowercase() }
    }

    override suspend fun checkHttp(url: String): HttpResult? = withContext(Dispatchers.IO) {
        runCatching {
            val response: HttpResponse = client.get(url)
            val status = response.status.value
            val finalUrl = response.request.url.toString()
            val headers = response.headers.entries().associate { it.key to it.value }
            // Ktor does not expose redirect history directly; we approximate using Location headers when present
            val redirects = buildList {
                response.headers.getAll("Location")?.let { addAll(it) }
            }
            HttpResult(statusCode = status, finalUrl = finalUrl, headers = headers, redirects = redirects)
        }.getOrNull()
    }

    override suspend fun fetchOg(url: String): OgResult? = withContext(Dispatchers.IO) {
        runCatching {
            val html = client.get(url).bodyAsText()
            val doc = Jsoup.parse(html, url)
            val title = doc.selectFirst("meta[property=og:title]")?.attr("content")
                ?: doc.title()
            val description = doc.selectFirst("meta[property=og:description]")?.attr("content")
                ?: doc.selectFirst("meta[name=description]")?.attr("content")
            val image = doc.selectFirst("meta[property=og:image]")?.attr("content")
            OgResult(title = title, description = description, image = image)
        }.getOrNull()
    }
}
