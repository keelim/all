package com.keelim.arducon.ui.screen.deeplink

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.common.Dispatcher
import com.keelim.common.KeelimDispatchers
import com.keelim.data.repository.ArduconRepository
import com.keelim.model.DeepLink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateDeepLinkViewModel @Inject constructor(
    private val repository: ArduconRepository,
    @Dispatcher(KeelimDispatchers.DEFAULT) private val default: CoroutineDispatcher,
) : ViewModel() {
    
    private val _scheme = MutableStateFlow("")
    val scheme: StateFlow<String> = _scheme.asStateFlow()
    
    private val _url = MutableStateFlow("")
    val url: StateFlow<String> = _url.asStateFlow()
    
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()
    
    private val _category = MutableStateFlow("")
    val category: StateFlow<String> = _category.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()
    
    val categories: StateFlow<List<String>> = repository.getCategories()
        .flowOn(default)
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5_000L), emptyList())
    
    fun setScheme(scheme: String) {
        _scheme.value = scheme
        // 스킴이 설정되면 기본 URL 형식 제안
        if (_url.value.isEmpty()) {
            _url.value = "$scheme://"
        }
    }
    
    fun updateUrl(url: String) {
        _url.value = url
    }
    
    fun updateTitle(title: String) {
        _title.value = title
    }
    
    fun updateCategory(category: String) {
        _category.value = category
    }
    
    fun createDeepLink() {
        if (_url.value.isBlank()) {
            return
        }
        
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val deepLink = DeepLink(
                    url = _url.value,
                    timestamp = System.currentTimeMillis(),
                    title = _title.value,
                    category = _category.value,
                )
                
                repository.insertDeepLinkUrl(deepLink)
                _isSuccess.value = true
                
                // 성공 후 입력 필드 초기화
                _url.value = ""
                _title.value = ""
                _category.value = ""
                
            } catch (e: Exception) {
                Timber.e(e, "딥링크 생성 실패")
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun resetSuccess() {
        _isSuccess.value = false
    }
    
    fun getFullUrl(): String {
        return _url.value
    }
    
    fun isValidUrl(): Boolean {
        val url = _url.value
        return url.isNotBlank() && (url.startsWith("http://") || url.startsWith("https://") || 
                url.startsWith("tel:") || url.startsWith("mailto:") || url.startsWith("sms:") ||
                url.startsWith("geo:") || url.startsWith("market:") || url.startsWith("intent:"))
    }
} 