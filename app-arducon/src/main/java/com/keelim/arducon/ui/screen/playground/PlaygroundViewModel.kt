package com.keelim.arducon.ui.screen.playground

import android.graphics.Bitmap
import com.keelim.common.qr.generateQrBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.repository.linkinspector.LinkInspectorRepository
import com.keelim.model.linkinspector.HttpResult
import com.keelim.model.linkinspector.OgResult
import com.keelim.model.linkinspector.ResolvedApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class PlaygroundUiState(
    val url: String = "",
    val paramKey: String = "",
    val paramValue: String = "",
    val preview: String = "",
    val resultText: String = "",
    val resolvedApps: List<ResolvedApp> = emptyList(),
    val http: HttpResult? = null,
    val og: OgResult? = null,
    val error: String? = null,
    val isLoading: Boolean = false,
)


@HiltViewModel
class PlaygroundViewModel @Inject constructor(
	private val repository: LinkInspectorRepository,
) : ViewModel() {

	private val _uiState = MutableStateFlow(PlaygroundUiState())
	val uiState: StateFlow<PlaygroundUiState> = _uiState

	fun updateUrl(url: String) {
		_uiState.value = _uiState.value.copy(url = url, preview = buildPreview(url, _uiState.value.paramKey, _uiState.value.paramValue))
	}

	fun updateParamKey(key: String) {
		_uiState.value = _uiState.value.copy(paramKey = key, preview = buildPreview(_uiState.value.url, key, _uiState.value.paramValue))
	}

	fun updateParamValue(value: String) {
		_uiState.value = _uiState.value.copy(paramValue = value, preview = buildPreview(_uiState.value.url, _uiState.value.paramKey, value))
	}

	private fun buildPreview(url: String, key: String, value: String): String {
		if (url.isBlank()) return ""
		if (key.isBlank() || value.isBlank()) return url
		val sep = if (url.contains("?")) "&" else "?"
        return url
	}

	fun validate() {
		viewModelScope.launch {
			val state = _uiState.value
			if (state.url.isBlank()) return@launch
			_uiState.value = state.copy(isLoading = true, resultText = "", error = null)
			val target = state.preview.ifBlank { state.url }
			val resolved = runCatching { repository.resolveApps(target) }.getOrElse { emptyList() }
			val http = runCatching { repository.checkHttp(target) }.getOrNull()
			val og = runCatching { repository.fetchOg(http?.finalUrl ?: target) }.getOrNull()
			val result = buildString {
				appendLine("Resolved Apps (${resolved.size}):")
                resolved.forEach { resolve -> appendLine(" - ${resolve.label} ${resolve.packageName})") }
                if (http != null) appendLine("HTTP: ${http.statusCode} → ${http.finalUrl}") else appendLine(
                    "HTTP: n/a"
                )
                if (og != null) appendLine("OG: ${og.title ?: "-"} | ${og.description ?: " - "}") else appendLine(
                    "OG: n/a"
                )
			}
			_uiState.value = _uiState.value.copy(
				isLoading = false,
				resultText = result,
				resolvedApps = resolved,
				http = http,
				og = og,
				error = null,
			)
		}
	}

	fun buildShareText(state: PlaygroundUiState): String {
		val sb = StringBuilder()
		sb.appendLine("Playground Report")
        sb.appendLine("URL: ${state.preview.ifBlank { state.url }}")
		sb.appendLine(state.resultText)
		return sb.toString()
	}

	// QR
	sealed interface QrDialogState {
		data object Hidden : QrDialogState
		data class Loading(val content: String) : QrDialogState
		data class Success(val content: String, val bitmap: Bitmap) : QrDialogState
		data class Error(val message: String) : QrDialogState
	}

	private val _qrDialogState = MutableStateFlow<QrDialogState>(QrDialogState.Hidden)
	val qrDialogState: StateFlow<QrDialogState> = _qrDialogState

	fun generateQrCode() {
		val content = _uiState.value.preview.ifBlank { _uiState.value.url }
		if (content.isBlank()) return
		_qrDialogState.value = QrDialogState.Loading(content)
		viewModelScope.launch {
			try {
				val bmp = withContext(Dispatchers.Default) { generateQrBitmap(content) }
				_qrDialogState.value = QrDialogState.Success(content, bmp)
			} catch (e: Exception) {
                _qrDialogState.value = QrDialogState.Error("QR 생성 실패: ${e.message}")
			}
		}
	}

	fun hideQrDialog() {
		_qrDialogState.value = QrDialogState.Hidden
	}

	// generateQrBitmap moved to core-common util
}

