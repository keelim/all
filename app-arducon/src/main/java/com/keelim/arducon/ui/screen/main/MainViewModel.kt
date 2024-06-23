package com.keelim.arducon.ui.screen.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.core.data.source.ArduconRepository
import com.keelim.core.database.model.DeepLink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ArduconRepository,
) : ViewModel() {
    private val _onClickSearch = MutableStateFlow("")
    val onClickSearch = _onClickSearch.asStateFlow()

    val deepLinkList: StateFlow<List<DeepLink>> = repository.getDeepLinkUrls()
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(), emptyList())

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
    ) {
        viewModelScope.launch {
            runCatching {
                repository.insertDeepLinkUrl(
                    DeepLink(
                        url = uri,
                        timestamp = System.currentTimeMillis(),
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
}
