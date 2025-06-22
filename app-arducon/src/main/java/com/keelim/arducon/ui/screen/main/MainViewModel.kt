package com.keelim.arducon.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.core.network.Dispatcher
import com.keelim.core.network.KeelimDispatchers
import com.keelim.data.repository.ArduconRepository
import com.keelim.model.DeepLink
import com.keelim.scheme.notification.SchemeNotificationManager
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private val defaultSchemeList = listOf(
    "http",
    "https",
)

@HiltViewModel
class MainViewModel @Inject constructor(
    @Dispatcher(KeelimDispatchers.DEFAULT) private val default: CoroutineDispatcher,
    private val repository: ArduconRepository,
    private val schemeNotificationManager: Lazy<SchemeNotificationManager>,
) : ViewModel() {
    private val _onClickSearch = MutableStateFlow("")
    val onClickSearch = _onClickSearch.asStateFlow()

    private val _selectedCategory = MutableStateFlow("")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val categories: StateFlow<List<String>> = repository.getCategories()
        .map { it.sorted() }
        .flowOn(default)
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5_000L), emptyList())

    val schemeList: StateFlow<List<String>> = repository.getSchemeList()
        .map {
            defaultSchemeList + it
        }
        .flowOn(default)
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5_000L), emptyList())

    val deepLinkList: StateFlow<Pair<List<DeepLink>, List<DeepLink>>> = repository.getDeepLinkUrls()
        .combine(_selectedCategory) { deeplinks, category ->
            val filtered = if (category.isEmpty()) {
                deeplinks
            } else {
                deeplinks.filter { it.category == category }
            }
            filtered.partition { deeplink -> deeplink.isBookMarked }
        }
        .flowOn(default)
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5_000L), Pair(emptyList(), emptyList()))

    private val _showBottomSheet = MutableStateFlow<DeepLink>(DeepLink.EMPTY)
    val showBottomSheet = _showBottomSheet.asStateFlow()

    private val _editDeepLink = MutableStateFlow<DeepLink?>(null)
    val editDeepLink = _editDeepLink.asStateFlow()

    // 딥링크 검색 버튼 클릭
    fun onClickSearch(
        uri: String,
        title: String,
        category: String,
    ) {
        viewModelScope.launch {
            runCatching {
                repository.insertDeepLinkUrl(
                    DeepLink(
                        url = uri,
                        timestamp = System.currentTimeMillis(),
                        title = title,
                        category = category,
                    ),
                )
            }.onSuccess {
                _onClickSearch.value = uri
            }.onFailure {
                Timber.d("onClickSearch() onError() -> " + it.localizedMessage)
                _onClickSearch.value = ""
            }
        }
    }

    fun updateDeepLinkUrl(deepLink: DeepLink) {
        viewModelScope.launch {
            runCatching {
                repository.updateDeepLinkUrl(deepLink.copy(isBookMarked = deepLink.isBookMarked.not()))
            }.onFailure {
                Timber.d("updateDeepLinkUrl() onError() -> " + it.localizedMessage)
            }
        }
    }

    fun deleteDeepLinkUrl(deepLink: DeepLink) {
        viewModelScope.launch {
            runCatching {
                repository.deleteDeepLinkUrl(deepLink)
            }.onFailure {
                Timber.d("deleteDeepLinkUrl() onError() -> " + it.localizedMessage)
            }
        }
    }

    fun clear() {
        _onClickSearch.value = ""
    }

    fun onRegister(scheme: String) {
        viewModelScope.launch {
            runCatching {
                repository.insertScheme(scheme)
            }.onFailure {
                Timber.d("onRegister() onError() -> " + it.localizedMessage)
            }
        }
    }

    fun deleteScheme(scheme: String) {
        viewModelScope.launch {
            runCatching {
                repository.deleteScheme(scheme)
            }.onFailure {
                Timber.d("deleteScheme() onError() -> " + it.localizedMessage)
            }
        }
    }

    fun onItemLongClick(deepLink: DeepLink) {
        _showBottomSheet.value = deepLink
    }

    fun hideBottomSheet() {
        _showBottomSheet.value = DeepLink.EMPTY
    }

    fun onEditDeepLink(deepLink: DeepLink) {
        _editDeepLink.value = deepLink
    }

    fun clearEditDeepLink() {
        _editDeepLink.value = null
    }

    fun updateSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun showNotification(
        notificationId: Int,
        title: String,
        message: String,
        deepLinkUri: String,
    ) {
        schemeNotificationManager.get().showDeepLinkNotification(
            notificationId = notificationId,
            title = title,
            message = message,
            deepLinkUri = deepLinkUri,
        )
    }
}
