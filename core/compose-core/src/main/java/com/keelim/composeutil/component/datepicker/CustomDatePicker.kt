import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
    OutlinedTextField(
        readOnly = true,
        value = date.format(DateTimeFormatter.ISO_DATE),
        label = { Text("Date") },
        onValueChange = {})

    IconButton(
        onClick = { setOpen(true) } // show de dialog
        ) {
          Icon(
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
        })
  }
}

@Composable
fun CustomDatePickerDialog(onAccept: (Long?) -> Unit, onCancel: () -> Unit) {
  val state = rememberDatePickerState()

  DatePickerDialog(
      onDismissRequest = {},
      confirmButton = {
        Button(onClick = { onAccept(state.selectedDateMillis) }) { Text("Accept") }
      },
      dismissButton = { Button(onClick = onCancel) { Text("Cancel") } }) {
        DatePicker(state = state)
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
