package com.keelim.arducon.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.core.network.Dispatcher
import com.keelim.core.network.KeelimDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OgTagPreviewViewModel @Inject constructor(
    @Dispatcher(KeelimDispatchers.IO) private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    fun parseOgTags(url: String, onComplete: (OgTagData) -> Unit) = viewModelScope.launch {
        try {
            withContext(dispatcher) {
                val doc: Document = Jsoup.connect(url).get()
                Timber.d("Document: $doc")
                val title = doc.select("meta[property=og:title]").attr("content")
                    .takeIf { it.isNotEmpty() } ?: doc.title()

                val description = doc.select("meta[property=og:description]").attr("content")
                    .takeIf { it.isNotEmpty() } ?: doc.select("meta[name=description]")
                    .attr("content")
                    .takeIf { it.isNotEmpty() } ?: ""

                val imageUrl = doc.select("meta[property=og:image]").attr("content")
                    .takeIf { it.isNotEmpty() } ?: ""

                Timber.d("OG Tags - Title: $title, Description: $description, Image URL: $imageUrl")
                onComplete(OgTagData(title, description, imageUrl))
            }
        } catch (e: Exception) {
            // 에러 처리
            Timber.d("Error parsing OG tags: ${e.message}")
            onComplete(OgTagData())
        }
    }

}
