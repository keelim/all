package com.keelim.mygrade.ui.screen.habit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class Todo(val id: Int, val label: String, initialChecked: Boolean = false) {
    var checked by mutableStateOf(initialChecked)
}
private fun getWellnessTodos() = List(30) { i -> Todo(i, "Todo # $i") }

@HiltViewModel
class TodoViewModel @Inject constructor(

): ViewModel() {
    private val _tasks = getWellnessTodos().toMutableStateList()
    val tasks: List<Todo>
        get() = _tasks

    fun changeTaskChecked(item: Todo, checked: Boolean) =
        tasks.find { it.id == item.id }?.let { task ->
            task.checked = checked
        }

    fun remove(item: Todo) {
        _tasks.remove(item)
    }
}

@Composable
fun WaterCounter(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        var count by rememberSaveable { mutableStateOf(0) }
        if (count > 0) {
            var showTask by rememberSaveable { mutableStateOf(true) }
            if (showTask) {
                WellnessTodo(
                    onClose = { showTask = false },
                    todoName = "Have you taken your 15 minute walk today?",
                    checked = false,
                    onCheckedChange = {},
                    modifier = modifier
                )
            }
            Text("You've had $count glasses.")
        }

        Row(Modifier.padding(top = 8.dp)) {
            Button(onClick = { count++ }, enabled = count < 10) { Text("Add one") }
            Button(
                onClick = { count = 0 },
                Modifier.padding(start = 8.dp)
            ) { Text("Clear water count") }
        }
    }
}

@Preview(showBackground = true, widthDp = 100, heightDp = 100)
@Composable
private fun PreviewWaterCounter() {
    WaterCounter()
}

@Composable
private fun WellnessTodo(
    todoName: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = todoName,
            modifier = Modifier
                .weight(1f)
                .padding(paddingValues = PaddingValues(start = 16.dp))
        )
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        IconButton(onClick = onClose) { Icon(Icons.Filled.Close, contentDescription = "Close") }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWellnessTodo() {
    WellnessTodo(todoName = "Todo", onClose = {}, checked = false, onCheckedChange = {})
}

@Composable
fun StatelessCounter(count: Int, onIncrement: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        if (count > 0) {
            Text("You've had $count glasses.")
        }
        Button(onClick = onIncrement, Modifier.padding(top = 8.dp), enabled = count < 10) {
            Text("Add one")
        }
    }
}

@Composable
fun StatefulCounter(modifier: Modifier = Modifier) {
    var count by rememberSaveable { mutableStateOf(0) }
    StatelessCounter(count, { count++ }, modifier)
}

@Composable
internal fun WellnessTodoList(
    list: List<Todo>,
    onCheckedTodo: (Todo, Boolean) -> Unit,
    onCloseTodo: (Todo) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(items = list, key = { item -> item.id }) { item ->
            WellnessTodo(
                todoName = item.label,
                checked = item.checked,
                onCheckedChange = { checked -> onCheckedTodo(item, checked) },
                onClose = { onCloseTodo(item) }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewWellnessTodoList() {
    WellnessTodoList(
        list = getWellnessTodos(),
        onCheckedTodo = { todo: Todo, b: Boolean -> },
        onCloseTodo = {},
    )
}

@Composable
fun HabitScreen(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel = hiltViewModel()
) {
    Column(modifier = modifier) {
        StatefulCounter()

        WellnessTodoList(
            list = viewModel.tasks,
            onCheckedTodo = viewModel::changeTaskChecked,
            onCloseTodo = viewModel::remove
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHabitScreen() {
    HabitScreen()
}
