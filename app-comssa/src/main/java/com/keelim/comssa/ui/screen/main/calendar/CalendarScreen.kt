package com.keelim.comssa.ui.screen.main.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DatePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CalendarRoute() {
  CalendarScreen()
}

@Composable
fun CalendarScreen(
    viewModel:CalendarViewModel = hiltViewModel()
) {
  val state = rememberDatePickerState()
    Column {
        DatePicker(state = state)
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCalendarScreen() {
  CalendarScreen()
}