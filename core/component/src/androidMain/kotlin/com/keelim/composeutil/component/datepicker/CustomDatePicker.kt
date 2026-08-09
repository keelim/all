package com.keelim.composeutil.component.datepicker

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.component.KuiDatePicker
import com.keelim.core.designsystem.component.KuiDatePickerDialog
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiIconButton
import com.keelim.core.designsystem.component.KuiOutlinedTextField
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun CustomDatePicker() {
    val (date, setDate) = remember { mutableStateOf(LocalDate.now()) }
    val (isOpen, setOpen) = remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        KuiOutlinedTextField(
            readOnly = true,
            value = date.format(DateTimeFormatter.ISO_DATE),
            label = { KuiText("Date") },
            onValueChange = {},
        )

        KuiIconButton(
            onClick = { setOpen(true) }, // show de dialog
        ) {
            KuiIcon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "null",
            )
        }
    }

    if (isOpen) {
        CustomDatePickerDialog(
            onAccept = {
                setOpen(false)
                if (it != null) { // Set the date
                    setDate(Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate())
                }
            },
            onCancel = {
                setOpen(false)
            },
        )
    }
}

@Composable
fun CustomDatePickerDialog(onAccept: (Long?) -> Unit, onCancel: () -> Unit) {
    val state = rememberDatePickerState()

    KuiDatePickerDialog(
        onDismissRequest = {},
        confirmButton = {
            KuiButton(onClick = { onAccept(state.selectedDateMillis) }) { KuiText("Accept") }
        },
        dismissButton = { KuiButton(onClick = onCancel) { KuiText("Cancel") } },
    ) {
        KuiDatePicker(state = state)
    }
}

@Preview
@Composable
fun PreviewCustomDatePicker() {
    CustomDatePicker()
}

@Preview
@Composable
fun PreviewCustomDatePickerDialog() {
    CustomDatePickerDialog(onAccept = {}, onCancel = {})
}
