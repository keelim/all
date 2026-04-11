package com.keelim.arducon.ui.screen.ogtag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.common.Dispatcher
import com.keelim.common.KeelimDispatchers
import com.keelim.data.repository.linkinspector.LinkInspectorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import jakarta.inject.Inject

@HiltViewModel
class OgTagPreviewViewModel @Inject constructor(
    @Dispatcher(KeelimDispatchers.IO) private val dispatcher: CoroutineDispatcher,
    private val linkInspectorRepository: LinkInspectorRepository,
) : ViewModel() {

    fun parseOgTags(url: String, onComplete: (OgTagData) -> Unit) = viewModelScope.launch {
        try {
            withContext(dispatcher) {
                val og = linkInspectorRepository.fetchOg(url)
                if (og == null) {
                    onComplete(OgTagData())
                    return@withContext
                }

                val title = og.title.orEmpty()
                val description = og.description.orEmpty()
                val imageUrl = og.image.orEmpty()
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
