package com.keelim.nandadiagnosis.ui.screen.length

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keelim.composeutil.component.chart.LengthChartPoint
import com.keelim.composeutil.component.chart.LengthLineChart
import com.keelim.model.LengthRecord
import java.time.LocalDate

@Composable
fun LengthScreen(
    viewModel: LengthViewModel = hiltViewModel()
) {
    var input by remember { mutableStateOf("") }
    val records by viewModel.records.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchRecords()
    }

    val chartPoints = records.map { LengthChartPoint(date = it.date, value = it.length) }

    Column(modifier = Modifier.padding(16.dp)) {
        // 입력 영역
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("(cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                val value = input.toFloatOrNull()
                if (value != null) {
                    viewModel.addRecord(
                        LengthRecord(
                            date = LocalDate.now().toString(),
                            length = value
                        )
                    )
                    input = ""
                }
            }) {
                Text("저장")
            }
        }
        Spacer(Modifier.height(24.dp))

        // 그래프
        LengthLineChart(
            mainColor = MaterialTheme.colorScheme.primary,
            subColor = MaterialTheme.colorScheme.primary,
            points = chartPoints,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))

        // 기록 리스트
        Text("기록 리스트", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        LazyColumn {
            items(records) { record ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${record.date} : ${record.length}cm")
                    IconButton(onClick = { viewModel.deleteRecord(record.date) }) {
                        Icon(Icons.Default.Delete, contentDescription = "삭제")
                    }
                }
            }
        }
    }
}
