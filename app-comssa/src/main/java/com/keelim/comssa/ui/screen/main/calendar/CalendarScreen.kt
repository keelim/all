package com.keelim.comssa.ui.screen.main.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DatePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CalendarRoute(
    viewModel: CalendarViewModel = hiltViewModel(),
) = trace("CalendarRoute") {
    CalendarScreen()
}

@Composable
fun CalendarScreen() = trace("CalendarScreen") {
    val state = rememberDatePickerState()
    Column {
        DatePicker(state = state)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCalendarScreen() {
    CalendarScreen()
}
