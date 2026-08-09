package com.keelim.arducon.ui.screen.urlshortener

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.core.data.repository.ShortenedUrlRepository
import com.keelim.shared.data.database.model.ShortenedUrlEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import jakarta.inject.Inject
import kotlin.random.Random

data class UrlShortenerUiState(
    val inputUrl: String = "",
    val inputTitle: String = "",
    val generatedShortCode: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val expirationDays: Int = 0,
)

@HiltViewModel
class UrlShortenerViewModel @Inject constructor(
    private val shortenedUrlRepository: ShortenedUrlRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UrlShortenerUiState())
    val uiState = _uiState.asStateFlow()

    val shortenedUrls = shortenedUrlRepository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            shortenedUrlRepository.deleteExpired(System.currentTimeMillis())
        }
    }

    fun updateInputUrl(url: String) {
        _uiState.update { it.copy(inputUrl = url, errorMessage = null) }
    }

    fun updateInputTitle(title: String) {
        _uiState.update { it.copy(inputTitle = title) }
    }

    fun updateExpirationDays(days: Int) {
        _uiState.update { it.copy(expirationDays = days) }
    }

    fun generateShortUrl() {
        val url = _uiState.value.inputUrl.trim()
        if (url.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "URL을 입력해주세요.") }
            return
        }

        if (!isValidUrl(url)) {
            _uiState.update { it.copy(errorMessage = "유효한 URL 형식이 아닙니다.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val shortCode = generateShortCode()
                val currentTime = System.currentTimeMillis()
                val expiresAt = if (_uiState.value.expirationDays > 0) {
                    currentTime + (_uiState.value.expirationDays * 24 * 60 * 60 * 1000L)
                } else {
                    0L
                }

                val entity = ShortenedUrlEntity(
                    originalUrl = url,
                    shortCode = shortCode,
                    title = _uiState.value.inputTitle.ifEmpty { extractDomain(url) },
                    createdAt = currentTime,
                    expiresAt = expiresAt,
                )
                shortenedUrlRepository.insert(entity)

                _uiState.update {
                    it.copy(
                        generatedShortCode = shortCode,
                        isLoading = false,
                        inputUrl = "",
                        inputTitle = "",
                        expirationDays = 0,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "생성 중 오류가 발생했습니다: ${e.message}",
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun recordClick(item: ShortenedUrlEntity) {
        viewModelScope.launch {
            shortenedUrlRepository.incrementClickCount(item.id, System.currentTimeMillis())
        }
    }

    fun deleteItem(item: ShortenedUrlEntity) {
        viewModelScope.launch {
            shortenedUrlRepository.delete(item)
        }
    }

    fun clearGeneratedCode() {
        _uiState.update { it.copy(generatedShortCode = "") }
    }

    private fun generateShortCode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..6).map { chars[Random.nextInt(chars.length)] }.joinToString("")
    }

    private fun isValidUrl(url: String): Boolean {
        return url.startsWith("http://") || url.startsWith("https://") ||
            url.contains("://")
    }

    private fun extractDomain(url: String): String {
        return try {
            url.removePrefix("https://")
                .removePrefix("http://")
                .substringBefore("/")
                .substringBefore("?")
        } catch (e: Exception) {
            url
        }
    }
}
