package com.keelim.setting.screen.theme

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.shared.data.ThemeType
import com.keelim.shared.data.UserStateStore
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

//Simple holder for theme type and its title (for radio buttons).
data class RadioItem(
    val value: ThemeType,
    val title: String,
)

//Holds selected theme and theme options.
data class ThemeTypeState(
    val selectedRadio: ThemeType,
    val isDialogVisible: Boolean = false,
    val items: List<RadioItem> = listOf(
        RadioItem(ThemeType.SYSTEM, "System"),
        RadioItem(ThemeType.LIGHT, "Light"),
        RadioItem(ThemeType.DARK, "Dark"),
    ),
)

@Stable
@HiltViewModel
class ThemeViewModel @Inject constructor(
    val userStateStore: Lazy<UserStateStore>,
) : ViewModel() {
    private val isDialogVisible = MutableStateFlow(false)
    // Observe the DataStore flow for theme type preference
    private val themeType: StateFlow<ThemeType> = userStateStore.get()
        .themeTypeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ThemeType.SYSTEM
        )

    val themeTypeState = isDialogVisible.combine(themeType) { isDialogVisible, selectedTheme ->
        ThemeTypeState(isDialogVisible = isDialogVisible, selectedRadio = selectedTheme)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ThemeTypeState(ThemeType.SYSTEM)
    )

    fun setDialogVisibility(value: Boolean) {
        isDialogVisible.update { value }
    }

    fun updateThemeType(themeType: ThemeType) {
        userStateStore.get().setThemeType(themeType, viewModelScope)
    }
}
