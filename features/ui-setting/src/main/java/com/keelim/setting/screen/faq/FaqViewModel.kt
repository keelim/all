package com.keelim.setting.screen.faq

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface FaqState {
    data object Loading : FaqState
    data object Error : FaqState
    data class Success(
        val faqItems: List<Faq>,
    ) : FaqState
}

data class Faq(val title: String, val desc: String)

@HiltViewModel
class FaqViewModel
@Inject
constructor(
    private val notificationRepository: NotificationRepository,
) : ViewModel() {
    val faqState: StateFlow<FaqState> =
        flow {
            emit(FaqState.Loading)
            notificationRepository
                .getNotification()
                .asSequence()
                .filter { it.faq }
                .map {
                    Faq(
                        it.title,
                        it.desc,
                    )
                }
                .toList()
                .let { faqItems ->
                    emit(FaqState.Success(faqItems))
                }
        }.catch { emit(FaqState.Error) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000L),
                FaqState.Success(listOf()),
            )
}
