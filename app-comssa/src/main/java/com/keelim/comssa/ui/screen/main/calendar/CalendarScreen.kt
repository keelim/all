package com.keelim.comssa.ui.screen.main.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DatePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.util.trace

@Composable
fun CalendarRoute() = trace("CalendarRoute") {
    CalendarScreen()
}

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel(),
) = trace("CalendarScreen") {
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
