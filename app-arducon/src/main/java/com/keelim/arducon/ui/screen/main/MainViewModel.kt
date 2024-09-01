package com.keelim.arducon.ui.screen.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.core.database.repository.ArduconRepository
import com.keelim.model.DeepLink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val repository: ArduconRepository,
) : ViewModel() {
    private val _onClickSearch = MutableStateFlow("")
    val onClickSearch = _onClickSearch.asStateFlow()

    val schemeList: StateFlow<List<String>> = repository.getSchemeList()
        .map {
            defaultSchemeList + it
        }
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(), emptyList())

    val deepLinkList: StateFlow<Pair<List<DeepLink>, List<DeepLink>>> = repository.getDeepLinkUrls()
        .map {
            it.partition { it.isBookMarked }
        }
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(), Pair(emptyList(), emptyList()))

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun showProgress() {
        _isLoading.value = true
    }

    fun hideProgress() {
        _isLoading.value = false
    }

    // 딥링크 검색 버튼 클릭
    fun onClickSearch(
        uri: String,
        title: String,
    ) {
        viewModelScope.launch {
            runCatching {
                repository.insertDeepLinkUrl(
                    DeepLink(
                        url = uri,
                        timestamp = System.currentTimeMillis(),
                        title = title,
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
                showProgress()
                repository.updateDeepLinkUrl(deepLink.copy(isBookMarked = deepLink.isBookMarked.not()))
            }.onFailure {
                Timber.d("updateDeepLinkUrl() onError() -> " + it.localizedMessage)
            }
            hideProgress()
        }
    }

    fun deleteDeepLinkUrl(deepLink: DeepLink) {
        viewModelScope.launch {
            runCatching {
                showProgress()
                repository.deleteDeepLinkUrl(deepLink)
            }.onFailure {
                Timber.d("deleteDeepLinkUrl() onError() -> " + it.localizedMessage)
            }
            hideProgress()
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
}
