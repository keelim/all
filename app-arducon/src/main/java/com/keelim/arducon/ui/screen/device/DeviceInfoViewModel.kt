package com.keelim.arducon.ui.screen.device

import android.os.Build
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DeviceInfoViewModel @Inject constructor() : ViewModel() {
    // Logic for retrieving device info can be moved here if it becomes more complex
    // For now, it's static data, but ViewModel is good for consistency.
}
