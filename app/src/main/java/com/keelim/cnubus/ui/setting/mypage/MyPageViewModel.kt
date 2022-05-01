/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.cnubus.ui.setting.mypage

import androidx.lifecycle.viewModelScope
import com.keelim.cnubus.data.db.entity.History
import com.keelim.cnubus.domain.UserUseCase
import com.keelim.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : BaseViewModel() {
    val userName = MutableStateFlow("id: 아직 로그인 중이 아닙니다.")
    val userFollowerCount = MutableStateFlow(0)
    val userFollowingCount = MutableStateFlow(0)
    private val _viewEvent = MutableSharedFlow<ViewEvent>(extraBufferCapacity = 16)
    val viewEvent: SharedFlow<ViewEvent> = _viewEvent.asSharedFlow()
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
        Timber.e(exception)
        viewModelScope.launch {
            _viewEvent.emit(ViewEvent.ShowToast(exception.toString()))
        }
    }

    init {
        init()
    }

    fun init() = viewModelScope.launch(errorHandler) {
        getUserId()
    }

    fun changeUserId(change: String) = viewModelScope.launch(errorHandler) {
        userUseCase.setUserName(change)
        getUserId()
    }

    fun getUserId() = viewModelScope.launch(errorHandler) {
        userUseCase.getUserName()
            .collectLatest {
                userName.emit(it.id)
            }
    }

    fun deleteHistory(history: History) = viewModelScope.launch(errorHandler) {
        userUseCase.deleteHistory(history)
    }

    fun deleteHistoryAll() = viewModelScope.launch(errorHandler) {
        userUseCase.deleteHistoryAll()
    }

    fun getAllHistories() = userUseCase.getAllHistories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    sealed class ViewEvent {
        data class ShowToast(val message: String) : ViewEvent()
    }
}
